package component;

import org.junit.Test;

public class UtXmlStreamComponentTest extends ComponentTestSupport {
    private static final String XML = "<ns1:container searchKey=\"test search key\" xmlns:ns1=\"http://testns\"> <ns1:id id=\"id 1\" xmlns:ns1=\"" +
            "http://testns2\">description first</ns1:id> <ns1:id id=\"id 2\" xmlns:ns1=\"http://testns2\">second description</ns1:id>  </ns1:container> ";

    @Test
    public void invoke_xmlStream() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT).prop("SearchKey", "test search key");
        PreparePageList idList = preparePage("PrimPage").pageList("IdList");
        idList.append(C_BASECLASS).prop(P_PY_ID, "id 1").prop(P_PY_DESCRIPTION, "description first");
        idList.append(C_BASECLASS).prop(P_PY_ID, "id 2").prop(P_PY_DESCRIPTION, "second description");
        preparePage("TestOutputPage").create(C_BASECLASS);

        invoke().xmlStream().name("Container").type("TestXmlStream").primaryPage("PrimPage");

        assertInvocationResult().string(XML);
    }

    @Test
    public void expect_xmlStream() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().xmlStream().className(C_WORK_UT).name("Container").type("TestXmlStream").andMock(new MockBehaviour<MockStreamContext>() {
            @Override
            public void process(MockStreamContext context) throws Exception {
                context.prepareResult().value(file("activity_xml_stream_out.xml"));
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestXmlStream").primaryPage("PrimPage");

        assertPage("TestOutputPage").exists().prop(P_PY_DESCRIPTION, file("activity_xml_stream_out.xml"));
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void innercom_xmlStream() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT).prop("SearchKey", "test search key");
        PreparePageList idList = preparePage("PrimPage").pageList("IdList");
        idList.append(C_BASECLASS).prop(P_PY_ID, "id 1").prop(P_PY_DESCRIPTION, "description first");
        idList.append(C_BASECLASS).prop(P_PY_ID, "id 2").prop(P_PY_DESCRIPTION, "second description");

        invoke().activity().className(C_WORK_UT).name("TestXmlStream").primaryPage("PrimPage");

        assertPage("TestOutputPage").exists().propXml(P_PY_DESCRIPTION, file("activity_xml_stream_out.xml"));
        assertActivityStatus().good().messageAny();
    }
}
