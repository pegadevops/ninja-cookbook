package component;

public class UtDataTransformComponentTest extends ComponentTestSupport {

    @Test
    public void expect_dataTransform() throws Exception {
        logTestStart();
        prepareParameter("listSize").value("5");
        preparePage("TestedPage").create(C_WORK_UT);
        expect().dataTransform().className(C_BASECLASS).name("NestedDataTransform").andMock(new MockBehaviour<MockDataTransformContext>() {
            @Override
            public void process(MockDataTransformContext context) {
                context.assertParameter("index").value("1");

                context.preparePage("Primary").prop(P_PY_ID, "Overriden 1");
            }
        });
        expect().dataTransform().className(C_WORK_UT).name("NestedDataTransform").andMock(new MockBehaviour<MockDataTransformContext>() {
            @Override
            public void process(MockDataTransformContext context) {
                context.preparePage("Primary").prop(P_PY_DESTINATION, "Overriden Destination");
            }
        });

        invoke().dataTransform().name("TopDataTransform").primaryPage("TestedPage");

        AssertPage assertPrimaryPage = assertPage("TestedPage");
        assertPrimaryPage.prop(P_PY_DESTINATION, "Overriden Destination");
        AssertPageList assertIdList = assertPrimaryPage.pageList("IdList");
        assertIdList.at(1).prop(P_PY_ID, "Overriden 1").propAbsent(P_PY_LABEL);
        assertIdList.at(2).prop(P_PY_ID, "Index: 2").propPresent(P_PY_LABEL);
        assertIdList.at(3).prop(P_PY_ID, "Index: 3").propPresent(P_PY_LABEL);
        assertIdList.at(4).prop(P_PY_ID, "Index: 4").propPresent(P_PY_LABEL);
        assertIdList.at(5).prop(P_PY_ID, "Index: 5").propPresent(P_PY_LABEL);
        assertIdList.size(5);
    }

    @Test
    public void invoke_dataTransform() throws Exception {
        logTestStart();
        prepareParameter("listSize").value("5");
        preparePage("TestedPage").create(C_WORK_UT);

        invoke().dataTransform().name("TopDataTransform").primaryPage("TestedPage");

        AssertPage assertPrimaryPage = assertPage("TestedPage");
        assertPrimaryPage.prop(P_PY_DESTINATION, "Test Destination");
        AssertPageList assertIdList = assertPrimaryPage.pageList("IdList");
        assertIdList.at(1).prop(P_PY_ID, "Index: 1").propPresent(P_PY_LABEL);
        assertIdList.at(2).prop(P_PY_ID, "Index: 2").propPresent(P_PY_LABEL);
        assertIdList.at(3).prop(P_PY_ID, "Index: 3").propPresent(P_PY_LABEL);
        assertIdList.at(4).prop(P_PY_ID, "Index: 4").propPresent(P_PY_LABEL);
        assertIdList.at(5).prop(P_PY_ID, "Index: 5").propPresent(P_PY_LABEL);
        assertIdList.size(5);
    }
}
