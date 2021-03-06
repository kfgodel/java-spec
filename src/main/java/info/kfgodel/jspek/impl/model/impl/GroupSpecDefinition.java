package info.kfgodel.jspek.impl.model.impl;

import info.kfgodel.jspek.impl.context.MappedContext;
import info.kfgodel.jspek.impl.model.DisabledStatus;
import info.kfgodel.jspek.impl.model.SpecElement;
import info.kfgodel.jspek.impl.model.SpecGroup;
import info.kfgodel.jspek.impl.model.SpecTest;
import info.kfgodel.jspek.impl.model.TestContextDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This type represents a spec group definition
 * Created by kfgodel on 12/07/14.
 */
public class GroupSpecDefinition extends SpecElementSupport implements SpecGroup {

  private DisabledStatus disabledState;
  private List<SpecElement> elements;
  private List<Runnable> beforeBlocks;
  private List<Runnable> afterBlocks;
  private TestContextDefinition testContext;

  @Override
  public boolean isEmpty() {
    return elements.isEmpty();
  }

  @Override
  public List<SpecGroup> getSubGroups() {
    List<SpecGroup> subGroups = elements.stream()
      .filter((element) -> element instanceof SpecGroup)
      .map((element) -> (SpecGroup) element)
      .collect(Collectors.toList());
    return subGroups;
  }

  @Override
  public List<SpecTest> getDeclaredTests() {
    List<SpecTest> declaredTest = elements.stream()
      .filter((element) -> element instanceof SpecTest)
      .map((element) -> (SpecTest) element)
      .collect(Collectors.toList());
    return declaredTest;
  }

  @Override
  public boolean isMarkedAsDisabled() {
    return disabledState.isDisabledConsidering(getContainerGroup());
  }

  @Override
  public void markAsDisabled() {
    this.disabledState = DisabledStatus.DISABLED;
  }

  @Override
  public void addSubGroup(GroupSpecDefinition addedGroup) {
    this.addContainedElement(addedGroup);
  }

  @Override
  public void addTest(TestSpecDefinition addedSpec) {
    this.addContainedElement(addedSpec);
  }

  @Override
  public void addBeforeBlock(Runnable aCodeBlock) {
    this.beforeBlocks.add(aCodeBlock);
  }

  @Override
  public void addAfterBlock(Runnable aCodeBlock) {
    this.afterBlocks.add(aCodeBlock);
  }

  @Override
  public Stream<SpecElement> getSpecElements() {
    return elements.stream();
  }

  @Override
  public boolean hasNoTests() {
    if (!getDeclaredTests().isEmpty()) {
      return false;
    }
    List<SpecGroup> subGroups = getSubGroups();
    for (SpecGroup subGroup : subGroups) {
      if (!subGroup.hasNoTests()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public TestContextDefinition getTestContext() {
    return testContext;
  }

  @Override
  public List<Runnable> getBeforeBlocks() {
    List<Runnable> containerBeforeBlocks = getContainerGroup().getBeforeBlocks();
    List<Runnable> inheritedBlocks = new ArrayList<>(containerBeforeBlocks.size() + beforeBlocks.size());
    inheritedBlocks.addAll(containerBeforeBlocks);
    inheritedBlocks.addAll(beforeBlocks);
    return inheritedBlocks;
  }

  @Override
  public List<Runnable> getAfterBlocks() {
    List<Runnable> containerAfterBlocks = getContainerGroup().getAfterBlocks();
    List<Runnable> inheritedBlocks = new ArrayList<>(containerAfterBlocks.size() + afterBlocks.size());
    inheritedBlocks.addAll(afterBlocks);
    inheritedBlocks.addAll(containerAfterBlocks);
    return inheritedBlocks;
  }


  private void addContainedElement(SpecElementSupport element) {
    this.elements.add(element);
    element.setContainerGroup(this);
  }

  @Override
  protected void setContainerGroup(SpecGroup containerGroup) {
    super.setContainerGroup(containerGroup);
    this.testContext.setParentDefinition(containerGroup.getTestContext());
  }

  public static GroupSpecDefinition create(String groupName) {
    GroupSpecDefinition groupSpec = new GroupSpecDefinition();
    groupSpec.setName(groupName);
    groupSpec.disabledState = DisabledStatus.ENABLED;
    groupSpec.elements = new ArrayList<>();
    groupSpec.beforeBlocks = new ArrayList<>();
    groupSpec.afterBlocks = new ArrayList<>();
    groupSpec.testContext = MappedContext.create();
    groupSpec.setContainerGroup(NullContainerGroup.create());
    return groupSpec;
  }

}
