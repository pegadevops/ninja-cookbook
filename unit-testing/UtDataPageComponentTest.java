package component;

public class UtDataPageComponentTest extends ComponentTestSupport {
    @Test
    public void expect_dataPage_lookup() throws Exception {
        logTestStart();
        prepareParameter("id1").value("2");
        preparePage("PrimPage").create(C_WORK_UT);
        expect().lookup().className(C_DATA_DATA_1).andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) throws Exception {
                context.assertParameter(P_PY_ID).value("2");

                context.preparePrimaryPage().prop(P_PY_LABEL, "Mocked record #2");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestDataPages2").primaryPage("PrimPage");

        assertPage("PrimPage").exists()
                .prop(P_PY_LABEL, "Mocked record #2");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void expect_dataPage_allSources() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().activity().className(C_WORK_UT).name("LoadTestData1").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) throws Exception {
                context.preparePrimaryPage()
                        .prop(P_PY_ID, "mocked data id 1")
                        .prop("SearchKey", "mocked data search key 1")
                        .page("TestNestedPage").create(C_BASECLASS).prop(P_PY_LABEL, "mocked nested label 1");
                context.preparePrimaryPage().pageList("TestPageList").append(C_DATA_DATA_1).prop(P_PY_ID, "mocked page list id 1");
                context.preparePrimaryPage().pageGroup("TestPageGroup").add("Sub1", C_DATA_DATA_1).prop(P_PY_ID, "mocked page group id 1");
            }
        });
        expect().dataTransform().className(C_WORK_UT).name("LoadTestData2").andMock(new MockBehaviour<MockDataTransformContext>() {
            @Override
            public void process(MockDataTransformContext context) throws Exception {
                assertParameter("param1").value("dp2 param");

                context.preparePrimaryPage().prop(P_PY_LABEL, "mocked data label 2");
            }
        });
        expect().lookup().className(C_DATA_DATA_1).andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) throws Exception {
                context.assertParameter(P_PY_ID).value("1");

                context.preparePrimaryPage().prop(P_PY_LABEL, "Mocked record #1");
            }
        });
        expect().reportDefinition().className(C_DATA_DATA_1).name("DataTableEditorReport").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) throws Exception {
                context.assertParameter(P_PY_PAGE_NAME).value("D_TestData7");

                PreparePageList results = context.preparePage("D_TestData7").pageList(P_PX_RESULTS);
                results.append(C_DATA_DATA_1).prop(P_PY_ID, "3").prop(P_PY_LABEL, "Mocked record #3");
                results.append(C_DATA_DATA_1).prop(P_PY_ID, "4").prop(P_PY_LABEL, "Mocked record #4");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestDataPages").primaryPage("PrimPage");

        assertPage("PrimPage").exists()
                .prop(P_PY_DESCRIPTION, "mocked data id 1 : mocked data search key 1 : mocked nested label 1")
                .prop(P_PY_DESTINATION, "mocked data label 2")
                .prop(P_PY_LABEL, "test data label 2 : dp3 param modified")
                .prop(P_PY_ID, "test data label 2 : dp3 param")
                .prop("SearchKey", "new search key")
                .prop("pyCaseID", "test data label 2 : dup id")
                .prop("pySubject", "Mocked record #1");
        AssertPageList testData7 = assertPage("TestData7").exists().pageList(P_PX_RESULTS).size(2);
        testData7.at(1).prop(P_PY_ID, "3").prop(P_PY_LABEL, "Mocked record #3");
        testData7.at(2).prop(P_PY_ID, "4").prop(P_PY_LABEL, "Mocked record #4");
        AssertPage dTestData1 = assertPage("D_TestData1").exists().prop(P_PY_ID, "mocked data id 1");
        dTestData1.pageList("TestPageList").size(1).at(1).prop(P_PY_ID, "mocked page list id 1");
        dTestData1.pageGroup("TestPageGroup").size(1).at("Sub1").prop(P_PY_ID, "mocked page group id 1");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_data_page_mock_assertion_fail() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().dataTransform().className(C_WORK_UT).name("LoadTestData2").andMock(new MockBehaviour<MockDataTransformContext>() {
            @Override
            public void process(MockDataTransformContext context) throws Exception {
                assertParameter("param1").value("incorrect value");

                context.preparePrimaryPage().prop(P_PY_LABEL, "mocked data label 2");
            }
        });
        expectException(AssertionError.class, "assertParamValue: param1");

        invoke().activity().className(C_WORK_UT).name("TestDataPages").primaryPage("PrimPage");
    }

    @Test
    public void activity_innercom_data_page() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);

        invoke().activity().className(C_WORK_UT).name("TestDataPages").primaryPage("PrimPage");

        assertPage("PrimPage").exists()
                .prop(P_PY_DESCRIPTION, "test data id 1 : test data search key 1 : test nested label 1")
                .prop(P_PY_DESTINATION, "test data label 2 : dp2 param")
                .prop(P_PY_LABEL, "test data label 2 : dp3 param modified")
                .prop(P_PY_ID, "test data label 2 : dp3 param")
                .prop("SearchKey", "new search key")
                .prop("pyCaseID", "test data label 2 : dup id")
                .prop("pySubject", "Record #1");
        AssertPageList testData7 = assertPage("TestData7").exists().pageList(P_PX_RESULTS).size(2);
        testData7.at(1).prop(P_PY_ID, "1").prop(P_PY_LABEL, "Record #1");
        testData7.at(2).prop(P_PY_ID, "2").prop(P_PY_LABEL, "Record #2");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_innercom_data_page_requestor() throws Exception {
        logTestStart();
        prepareParameter("id1").value("2");
        prepareParameter("id2").value("1");
        preparePage("PrimPage").create(C_WORK_UT);

        invoke().activity().className(C_WORK_UT).name("TestDataPages2").primaryPage("PrimPage");

        assertPage("PrimPage").exists()
                .prop(P_PY_LABEL, "Record #2");
        assertActivityStatus().good().messageAny();
    }
}
