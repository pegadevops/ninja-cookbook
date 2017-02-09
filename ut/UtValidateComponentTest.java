package component;

import org.junit.Test;

public class UtValidateComponentTest extends ComponentTestSupport {
    @Test
    public void expect_validate() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().validate().className(C_WORK_UT).name("TestValidate").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) throws Exception {
                context.preparePage("PrimPage").messages().add("test validation message 1").add("test v msg 2");
            }
        });
        expect().validate().className(C_WORK_UT).name("TestValidate").andMock(new MockBehaviour<MockActivityContext>() {
            @Override
            public void process(MockActivityContext context) throws Exception {
                context.preparePage("PrimPage").messages().clear();
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestValidate").primaryPage("PrimPage");
        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "** test validation message 1\n** test v msg 2");

        preparePage("PrimPage").prop("SearchKey", "some value");
        invoke().activity().className(C_WORK_UT).name("TestValidate").primaryPage("PrimPage");
        assertPage("PrimPage").prop(P_PY_DESCRIPTION, "");
    }

    @Test
    public void invoke_validate() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);

        invoke().validate().name("TestValidate").primaryPage("PrimPage");
        assertPage("PrimPage").messages().size(1).string().messageEquals(".SearchKey: This field may not be blank.");

        preparePage("PrimPage").prop("SearchKey", "some value");
        invoke().validate().name("TestValidate").primaryPage("PrimPage");
        assertPage("PrimPage").messages().empty();
    }
}
