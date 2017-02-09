package component;

import org.junit.Test;

/**
 * Component test for Ninja-UT Assert Page Api.
 */
public class UtAssertPageImplComponentTest extends ComponentTestSupport {
    @Test
    public void propXml_withXQuery() throws Exception {
        logTestStart();
        preparePage("TestPage").create(C_BASECLASS).prop(P_PY_DESCRIPTION, file("test.xml"));

        assertPage("TestPage").propXml(P_PY_DESCRIPTION, "<c><i>abcd</i><i>ef</i></c>", file("test.xq"));
    }

    @Test
    public void propXml_withXQuery_fail() throws Exception {
        logTestStart();
        preparePage("TestPage").create(C_BASECLASS).prop(P_PY_DESCRIPTION, file("test.xml"));
        expectException(AssertionError.class, "Expected text value 'xyz' but was 'abcd'");

        assertPage("TestPage").propXml(P_PY_DESCRIPTION, "<c><i>xyz</i><i>ef</i></c>", file("test.xq"));
    }
}
