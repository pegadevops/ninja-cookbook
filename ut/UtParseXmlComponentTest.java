package component;

import org.junit.Test;

public class UtParseXmlComponentTest extends ComponentTestSupport {
    @Test
    public void invoke_parseXml() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);

        invoke().parseXml().namespace("TestParseXml").elementName("Container").primaryPage("PrimPage").xml(file("activity_parse_xml_in.xml"));

        assertPage("PrimPage").prop("SearchKey", "test search key");
        AssertPageList idList = assertPage("PrimPage").pageList("IdList").size(2);
        idList.at(1).prop(P_PY_ID, "id 1").prop(P_PY_DESCRIPTION, "description first");
        idList.at(2).prop(P_PY_ID, "id 2").prop(P_PY_DESCRIPTION, "second description");
    }

    @Test
    public void expect_parseXml() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestInputPage").create(C_BASECLASS).prop(P_PY_DESCRIPTION, "mock xml");
        expect().parseXml().className(C_WORK_UT).namespace("TestParseXml").name("Container").andMock(new MockBehaviour<MockParseContext>() {
            @Override
            public void process(MockParseContext context) throws Exception {
                context.assertParameter(Constants.R_PARSE_XML_INPUT_VALUE).value("mock xml");

                context.preparePage("PrimPage").prop("SearchKey", "mocked search key");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestParseXml").primaryPage("PrimPage");

        assertPage("PrimPage").prop("SearchKey", "mocked search key");
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void innercom_parseXml() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("TestInputPage").create(C_BASECLASS).prop(P_PY_DESCRIPTION, file("activity_parse_xml_in.xml"));

        invoke().activity().className(C_WORK_UT).name("TestParseXml").primaryPage("PrimPage");

        assertPage("PrimPage").prop("SearchKey", "test search key");
        AssertPageList idList = assertPage("PrimPage").pageList("IdList");
        idList.at(1).prop(P_PY_ID, "id 1").prop(P_PY_DESCRIPTION, "description first");
        idList.at(2).prop(P_PY_ID, "id 2").prop(P_PY_DESCRIPTION, "second description");

        assertActivityStatus().good().messageAny();
    }
}
