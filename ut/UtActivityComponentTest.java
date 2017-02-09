package component;

public class UtActivityComponentTest extends ComponentTestSupport {
    @Test
    public void expect_activity() throws Exception {
        logTestStart();
        prepareParameter("inputParam1").value("param1");
        prepareParameter("inputParam2").value("20090121T200122.419 GMT");
        preparePage("ExistedTopLevelPage1").create(C_BASECLASS).prop(P_PY_ID, "existing id 123");
        expect().activity().className(C_WORK_UT).name("NestedActivity").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertParameter("inputPage").value("CreatedTopLevelPage1");

                context.preparePage("CreatedTopLevelPage1").prop(P_PY_DESCRIPTION, "Changed input page in nested");
                context.preparePage("CreatedTopLevelPage2").create(C_BASECLASS).prop(P_PY_DESCRIPTION, "Changed output page in nested");
                context.prepareParameter("outputPage").value("CreatedTopLevelPage2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TopActivity").primaryPage("");

        assertPage("CreatedTopLevelPage1").exists().prop(P_PY_LABEL, "label param1").prop(P_PY_DESCRIPTION, "Changed input page in nested");
        assertPage("CreatedTopLevelPage2").exists().prop(P_PY_DESCRIPTION, "Changed output page in nested");
        assertPage("ExistedTopLevelPage1").exists()
                .prop(P_PY_ID, "existing id 123")
                .prop(P_PY_DESCRIPTION, "Desc 20090121T200122.419 GMT")
                .prop(P_PY_DESTINATION, "Changed output page in nested");
        assertParameter("outputParam1").value("Page:CreatedTopLevelPage2");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void expect_activity_assertFail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expectException(AssertionError.class, "Fail now!");

        expect().objOpenByHandle().handle("pzInsKey").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) throws Exception {
                context.assertFail("Fail now!");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestAssertFail").primaryPage("PrimPage");
    }

    @Test
    public void expect_activity_class() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().activity().className(C_WORK_UT).name("NestedActivity").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.prepareParameter("param1").value("value #1");
            }
        });
        expect().activity().className(C_DATA_DATA_1).name("Update").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.prepareParameter("param2").value("value #2");
            }
        });
        expect().activity().className("System-Queue-TestJob").name("Job1").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.prepareParameter("param3").value("value #3");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestActivity2").primaryPage("PrimPage");

        assertParameter("param1").value("value #1");
        assertParameter("param2").value("value #2");
        assertParameter("param3").value("value #3");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void expect_activity_exception() throws Exception {
        logTestStart();
        prepareParameter("inputParam1").value("param1");
        prepareParameter("inputParam2").value("20090121T200122.419 GMT");
        preparePage("ExistedTopLevelPage1").create(C_BASECLASS);
        expect().activity().className(C_WORK_UT).name("NestedActivity").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertParameter("inputPage").value("CreatedTopLevelPage1");
                context.prepareResult().exception("Unexpected exception!!", IllegalArgumentException.class.getName());
            }
        });

        invoke().activity().className(C_WORK_UT).name("TopActivity").primaryPage("");

        assertParameter("outputParam1").value("Erred: ** Java Exception: \tjava.lang.IllegalArgumentException: Unexpected exception!!");
        assertActivityStatus().fail().messageContains("Unexpected exception!!");
    }

    @Test
    public void expect_activity_fail() throws Exception {
        logTestStart();
        prepareParameter("inputParam1").value("param1");
        prepareParameter("inputParam2").value("20090121T200122.419 GMT");
        preparePage("ExistedTopLevelPage1").create(C_BASECLASS);
        expect().activity().className(C_WORK_UT).name("NestedActivity").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertParameter("inputPage").value("CreatedTopLevelPage1");
                context.prepareResult().fail("Something strange happened");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TopActivity").primaryPage("");

        assertParameter("outputParam1").value("Failed** Something strange happened");
        assertActivityStatus().fail().messageContains("Something strange happened");
    }

    @Test
    public void expect_activity_mock_assertion_fail() throws Exception {
        logTestStart();
        prepareParameter("inputParam1").value("param1");
        prepareParameter("inputParam2").value("20090121T200122.419 GMT");
        preparePage("ExistedTopLevelPage1").create(C_BASECLASS);
        expect().activity().className(C_WORK_UT).name("NestedActivity").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertParameter("inputPage").value("IncorrectValue");
                context.prepareResult().fail("Something strange happened");
            }
        });
        expectException(AssertionError.class, "Line 7 assertParamValue");

        invoke().activity().className(C_WORK_UT).name("TopActivity").primaryPage("");
    }

    @Test
    public void expect_activity_logMessage() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().logMessage()
                .component("Rule_Obj_Activity.TestLogMessage.TVSOrg_TVSApp_Work_UT.Action")
                .message("[TestLogMessage] Error").level(LogLevel.ERROR)
                .andMock(new MockBehaviour<MockLogMessageContext>() {
                    @Override
                    public void process(MockLogMessageContext context) throws Exception {
                    }
                });
        expect().logMessage()
                .message("[TestLogMessage] InfoForced no component").level(LogLevel.INFO_FORCED)
                .andMock(new MockBehaviour<MockLogMessageContext>() {
                    @Override
                    public void process(MockLogMessageContext context) throws Exception {
                    }
                });
        expect().logMessage()
                .component("Rule_Obj_Activity.TestLogMessage.TVSOrg_TVSApp_Work_UT.Action")
                .level(LogLevel.INFO_FORCED)
                .andMock(new MockBehaviour<MockLogMessageContext>() {
                    @Override
                    public void process(MockLogMessageContext context) throws Exception {
                    }
                });
        expect().logMessage()
                .component("Rule_Obj_Activity.TestLogMessage.TVSOrg_TVSApp_Work_UT.Action")
                .message("[TestLogMessage] No level")
                .andMock(new MockBehaviour<MockLogMessageContext>() {
                    @Override
                    public void process(MockLogMessageContext context) throws Exception {
                    }
                });

        invoke().activity().className(C_WORK_UT).name("TestLogMessage").primaryPage("PrimPage");

        assertActivityStatus().good().messageAny();
    }

    @Test
    public void invoke_activity() throws Exception {
        logTestStart();
        prepareParameter("inputParam1").value("param1");
        prepareParameter("inputParam2").value("20090121T200122.419 GMT");
        preparePage("ExistedTopLevelPage1").create(C_BASECLASS).prop(P_PY_ID, "existing id 123");

        invoke().activity().className(C_WORK_UT).name("TopActivity").primaryPage("");

        assertPage("CreatedTopLevelPage1").exists().prop(P_PY_LABEL, "label param1").prop(P_PY_DESCRIPTION, "Changed input page in nested");
        assertPage("CreatedTopLevelPage2").exists().prop(P_PY_DESCRIPTION, "Changed output page in nested");
        assertPage("ExistedTopLevelPage1").exists()
                .prop(P_PY_ID, "existing id 123")
                .prop(P_PY_DESCRIPTION, "Desc 20090121T200122.419 GMT")
                .prop(P_PY_DESTINATION, "Changed output page in nested");
        assertParameter("outputParam1").value("Page:CreatedTopLevelPage2");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void invoke_activity_inExpect() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().activity().className(C_WORK_UT).name("TopActivity").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.preparePage("SomeTLP").create(C_BASECLASS);
                context.prepareParameter("inputPage").value("SomeTLP");
                invoke().activity().className(C_WORK_UT).name("NestedActivity").primaryPage("PrimPage");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRealMockReal").primaryPage("PrimPage");

        assertPage("SomeTLP").exists().prop(P_PY_DESCRIPTION, "Changed input page in nested");
        assertPage("CreatedTopLevelPage2").exists().prop(P_PY_DESCRIPTION, "Changed output page in nested");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void invoke_activity_messages() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().activity().className(C_WORK_UT).name("NestedActivity").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertPrimaryPage().messages().size(4);
                context.assertPrimaryPage().messages().string().messageEquals("** genuine1\n" +
                        "** genuine2\n" +
                        ".TestDate: ** genuine 3\n" +
                        "and a half\n" +
                        "This is\n" +
                        "a multi-line parametrized olala message");

                context.preparePrimaryPage().messages().clear();
            }
        });
        expect().activity().className(C_WORK_UT).name("NestedActivity").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertPrimaryPage().messages().empty();

                context.preparePrimaryPage().messages().add("root message 1");
                context.preparePrimaryPage().messages().add("TestDate", "message 2");
            }
        });
        expect().activity().className(C_WORK_UT).name("NestedActivity").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertPrimaryPage().messages().size(2);

                context.preparePrimaryPage().messages().clear();
                context.preparePrimaryPage().messages().add("root message 3");
                context.preparePrimaryPage().messages().add("TestDate", "message 4");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestMessages").primaryPage("PrimPage");

        AssertMessages assertMessages = assertPage("PrimPage")
                .prop(P_PY_DESCRIPTION, "** root message 1\n.TestDate: ** message 2")
                .prop(P_PY_DESTINATION, "** root message 3\n.TestDate: ** message 4").messages();
        assertMessages.size(2);
        assertMessages.string().messageContains("root");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void invoke_activity_messages_anyNotApplicable() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expectException(UnsupportedOperationException.class, "No sense to check <any> message on a page -- use size() / empty() on messages instead");

        invoke().activity().className(C_WORK_UT).name("TestPageMessages").primaryPage("PrimPage");

        assertPage("PrimPage").messages().string().messageAny();
    }

    @Test
    public void invoke_activity_innercom_validate() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);

        invoke().activity().className(C_WORK_UT).name("TestValidate").primaryPage("PrimPage");
        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ".SearchKey: This field may not be blank.");

        preparePage("PrimPage").prop("SearchKey", "some value");
        invoke().activity().className(C_WORK_UT).name("TestValidate").primaryPage("PrimPage");
        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "");
    }

    @Test
    public void invoke_activity_pxCallRetrieveReportData() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);

        invoke().activity().className(C_WORK_UT).name("TestPxCallRetrieveReportData").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "2").prop(P_PY_DESTINATION, "2");
    }

    @Test
    public void invoke_activity_byClassAndName_parentClass() {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);

        invoke().activity().name("PolymorphicActivity").className(C_WORK);

        assertParameter("invocationLevel").value("Parent");
    }

    @Test
    public void invoke_activity_byClassAndName_childClass() {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);

        invoke().activity().name("PolymorphicActivity").className(C_WORK_UT);

        assertParameter("invocationLevel").value("Child");
    }

    @Test
    public void invoke_activity_byPrimaryAndName_parentClass() {
        logTestStart();
        preparePage("PrimPage").create(C_WORK);

        invoke().activity().name("PolymorphicActivity").primaryPage("PrimPage");

        assertParameter("invocationLevel").value("Parent");
    }

    @Test
    public void invoke_activity_byPrimaryAndName_childClass() {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);

        invoke().activity().name("PolymorphicActivity").primaryPage("PrimPage");

        assertParameter("invocationLevel").value("Child");
    }

    /**
     * This test should find problems with nested calls of the same rule.
     * Such calls get already mocked objects/classes to perform. The application must handle it.
     */
    @Test
    public void invoke_activity_recursiveCall() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("depth").value("3");

        invoke().activity().className(C_WORK_UT).name("TestRecursiveCall").primaryPage("PrimPage");

        assertParameter("depth").value("0");
        assertActivityStatus().good().messageAny();
    }
}
