package component;

import org.junit.Test;

public class UtRdbComponentTest extends ComponentTestSupport {
    private static final String EX_MESSAGE = "test error message";
    private static final String FAIL_MESSAGE = "FAIL: ** test error message";
    private static final String ERR_MESSAGE = "ERROR: ** test error message";

    @Test
    public void activity_rdbOpen() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OPEN");
        expect().rdbOpen().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) throws Exception {
                context.preparePage("TempPage").create(C_DATA_DATA_1).prop(P_PY_DESCRIPTION, "mocked description");
                context.prepareResult().found("TempPage");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("TestData").exists().prop(P_PY_DESCRIPTION, "mocked description");
        assertPage("TempPage").notExists();
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_rdbOpen_notFound() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OPEN");
        expect().rdbOpen().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) throws Exception {
                context.prepareResult().notFound();
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertActivityStatus().fail().messageContains("not found");
    }

    @Test
    public void activity_rdbOpen_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OPEN");
        expect().rdbOpen().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) throws Exception {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_rdbOpen_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("OPEN");
        expect().rdbOpen().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjOpenContext>() {
            @Override
            public void process(MockObjOpenContext context) throws Exception {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_rdbList() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("LIST");
        expect().rdbList().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjBrowseContext>() {
            @Override
            public void process(MockObjBrowseContext context) throws Exception {
                PreparePageList pxResults = context.preparePage("TestDataList").prop(P_PX_RESULT_COUNT, "8").pageList(P_PX_RESULTS);
                pxResults.append(C_WORK).prop(P_PY_ID, "ID-1").prop(P_PY_LABEL, "Label 1");
                pxResults.append(C_WORK).prop(P_PY_ID, "ID-2").prop(P_PY_LABEL, "Label 2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("TestDataList").pageList(P_PX_RESULTS).size(2);
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_rdbList_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("LIST");
        expect().rdbList().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjBrowseContext>() {
            @Override
            public void process(MockObjBrowseContext context) {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_rdbList_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("LIST");
        expect().rdbList().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjBrowseContext>() {
            @Override
            public void process(MockObjBrowseContext context) {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_rdbSave() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("SAVE");
        expect().rdbSave().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext context) {
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_rdbSave_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("SAVE");
        expect().rdbSave().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext context) {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_rdbSave_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("SAVE");
        expect().rdbSave().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockObjSaveContext>() {
            @Override
            public void process(MockObjSaveContext context) {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_rdbDelete() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("DELETE");
        expect().rdbDelete().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) {
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_rdbDelete_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("DELETE");
        expect().rdbDelete().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) {
                context.prepareResult().fail(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, FAIL_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }

    @Test
    public void activity_rdbDelete_err() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        prepareParameter("mode").value("DELETE");
        expect().rdbDelete().className(C_DATA_DATA_1).requestType("TestSql").access("TestPackage").andMock(new MockBehaviour<MockDatabaseContext>() {
            @Override
            public void process(MockDatabaseContext context) {
                context.prepareResult().exception(EX_MESSAGE);
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestRdb").primaryPage("PrimPage");

        assertPage("PrimPage").prop(P_PY_DESCRIPTION, ERR_MESSAGE);
        assertActivityStatus().fail().messageContains(EX_MESSAGE);
    }
}
