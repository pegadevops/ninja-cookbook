package component;

public class UtDatabaseComponentTest extends ComponentTestSupport {
    private static final String EX_MESSAGE = "test error message";
    private static final String FAIL_MESSAGE = "FAIL: ** test error message";
    private static final String ERR_MESSAGE = "ERROR: ** test error message";

    @Test
    public void activity_objOpen() throws Exception {
        logTestStart();
        prepareParameter("mode").value("OO");
        expect().objOpen().className(C_WORK).page("TestPage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.assertPage("TestPage").prop(P_PY_ID, "123456");

                context.preparePage("TestPage2").create(C_WORK_UT)
                        .prop(P_PY_ID, "111")
                        .prop(P_PY_LABEL, "Loaded from DB");
                context.prepareResult().found("TestPage2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen");

        assertPage("TestPage").exists()
                .prop(P_PY_ID, "111")
                .prop(P_PY_LABEL, "Loaded from DB");
        assertPage("TestPage2").notExists();
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objOpen_not_found() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OO");
        expect().objOpen().className(C_WORK).page("TestPage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.assertPage("TestPage").prop(P_PY_ID, "123456");

                context.prepareResult().notFound();
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "NOT_FOUND");
        assertActivityStatus().fail().messageContains("Unable to open an instance using the given inputs");
    }

    @Test
    public void activity_objOpen_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OO");
        expect().objOpen().className(C_WORK).page("TestPage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.assertPage("TestPage").prop(P_PY_ID, "123456");

                context.prepareResult().fail(EX_MESSAGE);
                context.preparePage("TestPage2").create(C_WORK);
                context.prepareResult().found("TestPage2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objOpen_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OO");
        expect().objOpen().className(C_WORK).page("TestPage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.assertPage("TestPage").prop(P_PY_ID, "123456");

                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objOpenByHandle() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OOH");
        expect().objOpenByHandle().handle("123456").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.preparePage("TestPage2").create(C_WORK_UT)
                        .prop(P_PY_ID, "111")
                        .prop(P_PY_LABEL, "Loaded from DB");
                context.prepareResult().found("TestPage2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen").primaryPage("PrimPage");

        assertPage("TestPage").exists()
                .prop(P_PY_ID, "111")
                .prop(P_PY_LABEL, "Loaded from DB");
        assertPage("TestPage2").notExists();
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objOpenByHandle_notFound() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OOH");
        expect().objOpenByHandle().handle("123456").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.prepareResult().notFound();
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "NOT_FOUND");
        assertActivityStatus().fail().messageContains("Unable to open an instance using the given inputs");
    }

    @Test
    public void activity_objOpenByHandle_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OOH");
        expect().objOpenByHandle().handle("123456").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.prepareResult().fail(EX_MESSAGE);
                context.preparePage("TestPage2").create(C_WORK);
                context.prepareResult().found("TestPage2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objOpenByHandle_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OOH");
        expect().objOpenByHandle().handle("123456").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void objRefreshAndLock() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK).prop(P_PY_LABEL, "Stale value");
        prepareParameter("mode").value("ORL");
        expect().objRefreshAndLock().className(C_WORK).page("TestPage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.preparePage("TestPage2").create(C_WORK)
                        .prop(P_PY_ID, "111")
                        .prop(P_PY_LABEL, "Refreshed from DB");
                context.prepareResult().found("TestPage2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen").primaryPage("PrimPage");

        assertPage("TestPage").exists()
                .prop(P_PY_ID, "111")
                .prop(P_PY_LABEL, "Refreshed from DB");
        assertPage("TestPage2").notExists();
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void objRefreshAndLock_notStale() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK).prop(P_PY_LABEL, "Fresh value");
        prepareParameter("mode").value("ORL");
        expect().objRefreshAndLock().className(C_WORK).page("TestPage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) {
                context.preparePage("TestPage").copy("TestPage2");
                context.prepareResult().found("TestPage2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjOpen").primaryPage("PrimPage");

        assertPage("TestPage").exists().prop(P_PY_LABEL, "Fresh value");
        assertPage("TestPage2").notExists();
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objSave() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().objSave().className(C_WORK).page("TestPage").writeNow(false).andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext context) {
                context.assertPage("TestPage").prop(P_PY_LABEL, "test label");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjSave").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objSave_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().objSave().className(C_WORK).page("TestPage").writeNow(false).andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext context) {
                context.assertPage("TestPage").prop(P_PY_LABEL, "test label");

                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjSave").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objSave_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().objSave().className(C_WORK).page("TestPage").writeNow(false).andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext context) {
                context.assertPage("TestPage").prop(P_PY_LABEL, "test label");

                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjSave").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objSave_writeNow() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("writeNow").value("true");
        expect().objSave().className(C_WORK).page("TestPage").writeNow(true).andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext context) {
                context.assertPage("TestPage").prop(P_PY_LABEL, "test label");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjSave").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objSave_writeNow_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("writeNow").value("true");
        expect().objSave().className(C_WORK).page("TestPage").writeNow(true).andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext context) {
                context.assertPage("TestPage").prop(P_PY_LABEL, "test label");

                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjSave").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objSave_writeNow_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("writeNow").value("true");
        expect().objSave().className(C_WORK).page("TestPage").writeNow(true).andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext context) {
                context.assertPage("TestPage").prop(P_PY_LABEL, "test label");

                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjSave").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objBrowse() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().objBrowse().className(C_WORK).page("TestList").andMock(new MockBehaviour<MockObjBrowseContext>() {
            @Override
            public void process(MockObjBrowseContext context) {
                PreparePageList pxResults = context.preparePage("TestList").prop(P_PX_RESULT_COUNT, "8").pageList(P_PX_RESULTS);
                pxResults.append(C_WORK).prop(P_PY_ID, "ID-1").prop(P_PY_LABEL, "Label 1");
                pxResults.append(C_WORK).prop(P_PY_ID, "ID-2").prop(P_PY_LABEL, "Label 2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjBrowse").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS: 8 = 2");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objBrowse_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().objBrowse().className(C_WORK).page("TestList").andMock(new MockBehaviour<MockObjBrowseContext>() {
            @Override
            public void process(MockObjBrowseContext context) {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjBrowse").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objBrowse_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().objBrowse().className(C_WORK).page("TestList").andMock(new MockBehaviour<MockObjBrowseContext>() {
            @Override
            public void process(MockObjBrowseContext context) {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjBrowse").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objDelete() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK);
        expect().objDelete().className(C_WORK).page("TestPage").immediate(false).andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) throws Exception {
                // success
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjDelete").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objDelete_immediate() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK);
        prepareParameter("immediate").value("true");
        expect().objDelete().className(C_WORK).page("TestPage").immediate(true).andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) throws Exception {
                // success
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjDelete").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objDelete_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK);
        expect().objDelete().className(C_WORK).page("TestPage").immediate(false).andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) throws Exception {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjDelete").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objDelete_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK);
        expect().objDelete().className(C_WORK).page("TestPage").immediate(false).andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) throws Exception {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjDelete").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objDeleteByHandle() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK);
        prepareParameter("byHandle").value("true");
        expect().objDeleteByHandle().handle("123456").immediate(false).andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) throws Exception {
                // success
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjDelete").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objDeleteByHandle_immediate() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK);
        prepareParameter("byHandle").value("true");
        prepareParameter("immediate").value("true");
        expect().objDeleteByHandle().handle("123456").immediate(true).andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) throws Exception {
                // success
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjDelete").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_objDeleteByHandle_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK);
        prepareParameter("byHandle").value("true");
        expect().objDeleteByHandle().handle("123456").immediate(false).andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) throws Exception {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjDelete").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_objDeleteByHandle_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestPage").create(C_WORK);
        prepareParameter("byHandle").value("true");
        expect().objDeleteByHandle().handle("123456").immediate(false).andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) throws Exception {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestObjDelete").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_commit() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("commit").value("true");
        expect().commit().andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) {
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestCommitRollback").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_commit_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("commit").value("true");
        expect().commit().andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestCommitRollback").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_commit_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("commit").value("true");
        expect().commit().andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestCommitRollback").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_rollback() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().rollback().andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) {
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestCommitRollback").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "SUCCESS");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_rollback_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().rollback().andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestCommitRollback").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_rollback_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().rollback().andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestCommitRollback").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }
}
