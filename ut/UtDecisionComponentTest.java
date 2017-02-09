package component;

public class UtDecisionComponentTest extends ComponentTestSupport {
    @Test
    public void expect_decisionTable() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("TABLE");
        expect().decisionTable().className(C_WORK_UT).name("TestDecisionTable").andMock(new MockBehaviour<MockDecisionContext<String>>() {
            @Override
            public void process(MockDecisionContext<String> context) throws Exception {
                context.prepareResult().value("mocked decision result");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestDecision").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "mocked decision result");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void expect_decisionTree() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("TREE");
        expect().decisionTree().className(C_WORK_UT).name("TestDecisionTree").andMock(new MockBehaviour<MockDecisionContext<String>>() {
            @Override
            public void process(MockDecisionContext<String> context) throws Exception {
                context.prepareResult().value("mocked decision result");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestDecision").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "mocked decision result");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void expect_when() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("WHEN");
        expect().when().className(C_DATA_DATA_1).name("Never").andMock(new MockBehaviour<MockDecisionContext<Boolean>>() {
            @Override
            public void process(MockDecisionContext<Boolean> context) throws Exception {
                context.prepareResult().value(true);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestDecision").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "WHEN TRUE");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void invoke_decisionTable() {
        logTestStart();
        PreparePage primary = preparePage("PrimPage");
        primary.create(C_WORK_UT);

        // tests in real applications should have separate methods in test for every branch

        invoke().decisionTable().name("TestDecisionTable").primaryPage("PrimPage").allowMissingProperties(true);
        assertInvocationResult().string("decision 3");

        primary.prop("SearchKey", "search key 1");
        invoke().decisionTable().name("TestDecisionTable").primaryPage("PrimPage").allowMissingProperties(false);
        assertInvocationResult().string("decision 1");

        primary.prop("SearchKey", "search key 2");
        primary.pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id 1");
        invoke().decisionTable().name("TestDecisionTable").primaryPage("PrimPage").allowMissingProperties(false);
        assertInvocationResult().string("decision 3");

        primary.pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id 2");
        invoke().decisionTable().name("TestDecisionTable").primaryPage("PrimPage").allowMissingProperties(false);
        assertInvocationResult().string("decision 2");
    }

    @Test
    public void invoke_decisionTree() {
        logTestStart();
        PreparePage primary = preparePage("PrimPage");
        primary.create(C_WORK_UT);

        // tests in real applications should have separate methods in test for every branch

        invoke().decisionTree().name("TestDecisionTree").primaryPage("PrimPage").input("").allowMissingProperties(true);
        assertInvocationResult().string("decision 3");

        primary.prop("SearchKey", "search key 1");
        invoke().decisionTree().name("TestDecisionTree").primaryPage("PrimPage").input("").allowMissingProperties(false);
        assertInvocationResult().string("decision 1");

        primary.prop("SearchKey", "search key 2");
        primary.pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id 1");
        invoke().decisionTree().name("TestDecisionTree").primaryPage("PrimPage").input("").allowMissingProperties(false);
        assertInvocationResult().string("decision 3");

        primary.pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id 2");
        invoke().decisionTree().name("TestDecisionTree").primaryPage("PrimPage").input("").allowMissingProperties(false);
        assertInvocationResult().string("decision 2");
    }

    @Test
    public void invoke_when() {
        logTestStart();
        PreparePage primary = preparePage("PrimPage");
        primary.create(C_WORK_UT);

        // tests in real applications should have separate methods in test for every branch

        invoke().when().name("TestWhen").primaryPage("PrimPage");
        assertInvocationResult().booleanO(false);

        primary.prop("SearchKey", "search key 1");
        invoke().when().name("TestWhen").primaryPage("PrimPage");
        assertInvocationResult().booleanO(true);

        primary.prop("SearchKey", "search key 2");
        primary.pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id 1");
        invoke().when().name("TestWhen").primaryPage("PrimPage");
        assertInvocationResult().booleanO(false);

        primary.pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id 2");
        invoke().when().name("TestWhen").primaryPage("PrimPage");
        assertInvocationResult().booleanO(true);
    }
}
