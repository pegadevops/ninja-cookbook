package component;

public class UtConnectComponentTest extends ComponentTestSupport {
    @Test
    public void expect_connectJms() throws Exception {
        logTestStart();
        preparePage("TestPage").create(C_WORK_UT);
        prepareParameter("mode").value("JMS");
        expect().connectJms().andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertPage("TestPage").prop(P_PY_LABEL, "Request Contents");

                context.preparePage("TestPage").prop(P_PY_DESTINATION, "Response Contents");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestConnect").primaryPage("TestPage");

        assertPage("TestPage").prop(P_PY_DESCRIPTION, "Received Response Contents");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void expect_connectJms_fail() throws Exception {
        logTestStart();
        preparePage("TestPage").create(C_WORK_UT);
        prepareParameter("mode").value("JMS");
        expect().connectJms().andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertPage("TestPage").prop(P_PY_LABEL, "Request Contents");

                context.prepareResult().fail("Connection timeout: connect");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestConnect").primaryPage("TestPage");

        assertPage("TestPage").prop(P_PY_DESCRIPTION, "Failed ** Connection timeout: connect");
        assertActivityStatus().fail().messageContains("timeout");
    }

    @Test
    public void expect_connectSoap() throws Exception {
        logTestStart();
        PreparePage testPage = preparePage("TestPage").create(C_WORK_UT)
                .prop("SearchKey", "Request Search Key");
        PreparePageList testPageList = testPage.pageList("TestPageList");
        testPageList.append(C_DATA_DATA_1).prop(P_PY_ID, "Request ID #1");
        testPageList.append(C_DATA_DATA_1).prop(P_PY_ID, "Request ID #2");
        prepareParameter("mode").value("SOAP");
        expect().connectSoap().className(C_WORK_UT).service("TestConnect").andMock(new MockBehaviour<MockConnectContext>() {
            @Override
            public void process(MockConnectContext context) {
                context.assertParameter(Constants.R_CONNECT_REQUEST_VALUE).valueXml(file("soap_request.xml"));

                context.prepareParameter(Constants.R_CONNECT_RESPONSE_VALUE).value(file("soap_response.xml"));
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestConnect").primaryPage("TestPage");

        AssertPage assertPage = assertPage("TestPage");
        testPage.prop("SearchKey", "Response Search Key");
        AssertPageList idList = assertPage.pageList("IdList");
        idList.at(1).prop(P_PY_ID, "id 1").prop(P_PY_DESCRIPTION, "description first");
        idList.at(2).prop(P_PY_ID, "id 2").prop(P_PY_DESCRIPTION, "second description");
        idList.size(2);
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void expect_connectSoap_assertFail() throws Exception {
        logTestStart();
        preparePage("TestPage").create(C_WORK_UT).prop("SearchKey", "Request Search Key");
        prepareParameter("mode").value("SOAP");
        expect().connectSoap().className(C_WORK_UT).service("TestConnect").andMock(new MockBehaviour<MockConnectContext>() {
            @Override
            public void process(MockConnectContext context) {
                context.assertParameter(Constants.R_CONNECT_REQUEST_VALUE).valueXml(file("soap_request.xml"));
            }
        });
        expectException(AssertionError.class, "Line 7 assertParamValueXml: Expected child nodelist length '2' but was '0'");

        invoke().activity().className(C_WORK_UT).name("TestConnect").primaryPage("TestPage");
    }

    @Test
    public void expect_connectHttp() throws Exception {
        logTestStart();
        preparePage("TestPage").create(C_WORK_UT);
        prepareParameter("mode").value("HTTP");
        expect().connectHttp().andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) {
                context.assertPage("TestPage").prop(P_PY_LABEL, "Request Contents");

                context.preparePage("TestPage").prop(P_PY_DESTINATION, "Response Contents");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestConnect").primaryPage("TestPage");

        assertPage("TestPage").prop(P_PY_DESCRIPTION, "Received Response Contents");
        assertActivityStatus().good().messageAny();
    }
}
