package component;

import org.junit.Test;

public class UtQueueForAgentComponentTest extends ComponentTestSupport {
    private static final String EX_MESSAGE = "test error message";
    private static final String FAIL_MESSAGE = "FAIL: ** test error message";
    private static final String ERR_MESSAGE = "ERROR: ** test error message";

    @Test
    public void expect_queueForAgent() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().queueForAgent().ruleSet("TVSApp_Dev").name("Agent1").deferred(false).andMock(new MockBehaviour<MockQueueForAgentContext>() {
            @Override
            public void process(MockQueueForAgentContext context) {
                context.assertPage("TestJob").dump()
                        .prop("WorkID", "ABC-123")
                        .prop("TestLabel", "test label");
                context.preparePage("TestJob").copy("TestJob2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestAgent").primaryPage("PrimPage");

        assertPage("TestJob2").exists()
                .prop("WorkID", "ABC-123")
                .prop("TestLabel", "test label")
                .dump();
        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void expect_queueForAgent_onlyName() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().queueForAgent().name("Agent1").deferred(false).andMock(new MockBehaviour<MockQueueForAgentContext>() {
            @Override
            public void process(MockQueueForAgentContext context) {
                context.assertPage("TestJob").dump()
                        .prop("WorkID", "ABC-123")
                        .prop("TestLabel", "test label");
                context.preparePage("TestJob").copy("TestJob2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestAgent").primaryPage("PrimPage");

        assertPage("TestJob2").exists()
                .prop("WorkID", "ABC-123")
                .prop("TestLabel", "test label")
                .dump();
        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void expect_queueForAgent_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().queueForAgent().ruleSet("TVSApp_Dev").name("Agent1").deferred(false).andMock(new MockBehaviour<MockQueueForAgentContext>() {
            @Override
            public void process(MockQueueForAgentContext context) {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestAgent").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void expect_queueForAgent_exception() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().queueForAgent().ruleSet("TVSApp_Dev").name("Agent1").deferred(false).andMock(new MockBehaviour<MockQueueForAgentContext>() {
            @Override
            public void process(MockQueueForAgentContext context) {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestAgent").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }
}
