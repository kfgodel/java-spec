package ar.com.dgarcia.javaspec.impl.model.impl;

import ar.com.dgarcia.javaspec.api.contexts.TestContext;
import ar.com.dgarcia.javaspec.api.variable.Variable;
import ar.com.dgarcia.javaspec.impl.context.MappedContext;
import ar.com.dgarcia.javaspec.impl.model.DisabledStatus;
import ar.com.dgarcia.javaspec.impl.model.SpecElement;
import ar.com.dgarcia.javaspec.impl.model.SpecGroup;
import ar.com.dgarcia.javaspec.impl.model.SpecTest;
import ar.com.dgarcia.javaspec.impl.model.TestContextDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This type represents a spec group definition
 * Created by kfgodel on 12/07/14.
 */
public class SpecGroupDefinition extends SpecElementSupport implements SpecGroup {

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
    public SpecGroup createGroup(String aGroupName) {
        SpecGroupDefinition subgroup = SpecGroupDefinition.create(aGroupName, this);
        this.addContainedElement(subgroup);
        return subgroup;
    }

  @Override
  public SpecTest createTest(String testName, Optional<Runnable> testCode, Variable<TestContext> sharedContext) {
    SpecTestDefinition createdTestSpec = SpecTestDefinition.create(testName, testCode, sharedContext, this);
    this.addContainedElement(createdTestSpec);
    return createdTestSpec;
  }

    @Override
    public void addTest(SpecTestDefinition addedSpec) {
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
    public List<SpecElement> getSpecElements() {
        return elements;
    }

    @Override
    public boolean hasNoTests() {
        if(getDeclaredTests().size() > 0){
            return false;
        }
        List<SpecGroup> subGroups = getSubGroups();
        for (SpecGroup subGroup : subGroups) {
            if(!subGroup.hasNoTests()){
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
    }

    @Override
    protected void setContainerGroup(SpecGroup containerGroup) {
        super.setContainerGroup(containerGroup);
        this.testContext.setParentDefinition(containerGroup.getTestContext());
    }

    public static SpecGroupDefinition create(String groupName, SpecGroup parentContainer) {
        SpecGroupDefinition groupSpec = new SpecGroupDefinition();
        groupSpec.setName(groupName);
        groupSpec.disabledState = DisabledStatus.ENABLED;
        groupSpec.elements = new ArrayList<>();
        groupSpec.beforeBlocks = new ArrayList<>();
        groupSpec.afterBlocks = new ArrayList<>();
        groupSpec.testContext = MappedContext.create();
        groupSpec.setContainerGroup(parentContainer);
        return groupSpec;
    }

}
