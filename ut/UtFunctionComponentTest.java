package component;

import org.junit.Test;
import java.math.BigDecimal;

/**
 * Component test for Ninja-UT Pega Functions.
 */
public class UtFunctionComponentTest extends ComponentTestSupport {
    @Test
    public void expect_function_fromActivity() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        expect().function().name("DateTimeDuration").andMock(new MockBehaviour<MockFunctionContext>() {
            @Override
            public void process(MockFunctionContext context) throws Exception {
                context.prepareResult().string("mock function result");
            }
        });

        invoke().activity().className(C_WORK_UT).name("TestFunction").primaryPage("PrimPage");

        assertPage("PrimPage").exists()
                .prop(P_PY_LABEL, "3.33")
                .prop(P_PY_DESCRIPTION, "mock function result")
                .prop(P_PY_DESTINATION, "2009-01--2010-01");
    }

    @Test
    public void expect_function_fromDecisionTable() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT)
                .prop("SearchKey", "search key 2")
                .pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id 1");
        expect().function().name("LengthOfPageList").andMock(new MockBehaviour<MockFunctionContext>() {
            @Override
            public void process(MockFunctionContext context) throws Exception {
                context.prepareResult().integerO(2);
            }
        });

        invoke().decisionTable().name("TestDecisionTable").primaryPage("PrimPage").allowMissingProperties(false);

        assertInvocationResult().string("decision 2");
    }

    @Test
    public void expect_function_fromDecisionTree() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT)
                .prop("SearchKey", "search key 2")
                .pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id 1");
        expect().function().name("LengthOfPageList").andMock(new MockBehaviour<MockFunctionContext>() {
            @Override
            public void process(MockFunctionContext context) throws Exception {
                context.prepareResult().integerO(2);
            }
        });

        invoke().decisionTree().name("TestDecisionTree").primaryPage("PrimPage").allowMissingProperties(false);

        assertInvocationResult().string("decision 2");
    }

    @Test
    public void expect_function_fromWhen() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT)
                .prop("SearchKey", "search key 2")
                .pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id 1");
        expect().function().name("LengthOfPageList").andMock(new MockBehaviour<MockFunctionContext>() {
            @Override
            public void process(MockFunctionContext context) throws Exception {
                context.prepareResult().integerO(2);
            }
        });

        invoke().when().name("TestWhen").primaryPage("PrimPage");

        assertInvocationResult().booleanO(true);
    }

    @Test
    public void invoke_function() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT);
        preparePage("StPage").create(C_DATA_DATA_1);

        invoke().function().decoratedName("divide--(com.pega.ibm.icu.math.BigDeca49d5b1937938dbcb5d7dee848eb64e8").args()
                .icuBigDecimal(new BigDecimal("9876543210.12345"))
                .icuBigDecimal(new BigDecimal("1234.5678"))
                .integerO(3);
        assertInvocationResult().icuBigDecimal(new BigDecimal("8000000.656"));

        invoke().function().decoratedName("pxPrimaryPageGetReference--(Activity)").primaryPage("PrimPage").args()
                .publicApi();
        assertInvocationResult().string("PrimPage");

        invoke().function().decoratedName("pxGetStepPageReference--()").primaryPage("PrimPage").stepPage("StPage");
        assertInvocationResult().string("StPage");
    }

    @Test
    public void invoke_function_byLibraryAndRuleSetName() {
        logTestStart();

        invoke().function().name("formatLogString").library("TVSOrgUtils").ruleSet("TVSOrg").args().string("unformatted log");
        assertInvocationResult().string("[TVSOrg]: unformatted log");

        invoke().function().name("formatLogString").library("TVSAppUtils").ruleSet("TVSApp_Dev").args().string("unformatted log");
        assertInvocationResult().string("[TVSApp]: unformatted log");
    }

    @Test
    public void invoke_function_byName_null() {
        logTestStart();

        invoke().function().name("formatLogString").library("TVSOrgUtils").ruleSet("TVSOrg").args().nullO();

        assertInvocationResult().string("[TVSOrg]: null");
    }

    @Test
    public void invoke_function_byName_bigDecimalInteger() {
        logTestStart();

        invoke().function().name("divide").args()
                .icuBigDecimal(new BigDecimal("9876543210.12345"))
                .icuBigDecimal(new BigDecimal("1234.5678"))
                .integerO(3);

        assertInvocationResult().icuBigDecimal(new BigDecimal("8000000.656"));
    }

    @Test
    public void invoke_function_byName_booleanString() {
        logTestStart();

        // ValidateJavaIdentifier(String thValue, boolean allowDashes) returns true if theValue is valid
        invoke().function().name("ValidateJavaIdentifier").args()
                .string("abc-def")
                .booleanO(false);
        assertInvocationResult().booleanO(false);

        invoke().function().name("ValidateJavaIdentifier").args()
                .string("abc-def")
                .booleanO(true);
        assertInvocationResult().booleanO(true);
    }

    @Test
    public void invoke_function_byName_notFound() {
        logTestStart();
        expectException(RemotePegaException.class, "Invocation error");

        invoke().function().name("UnknownFunction");
    }

    @Test
    public void invoke_function_byName_clipboardPage() {
        logTestStart();
        preparePage("TempPage").create("Work-").page("pyCaseUpdateInfo").create("Embed-CaseUpdate");
        preparePage("TempPage").page("pyInboundEmail").create("Embed-Services-InboundEmailInfo");

        invoke().function().name("pxGetParentPage").args()
                .clipboardPage("TempPage.pyCaseUpdateInfo");

        assertInvocationResult().clipboardPage("TempPage");
    }

    @Test
    public void invoke_function_byName_clipboardProperty() {
        logTestStart();
        preparePage("TempPage").create("Work-").prop("pyLabel", "label");

        invoke().function().name("PropertyHasValue").ruleSet("Pega-RULES").library("Utilities").args()
                .clipboardProperty("TempPage.pyLabel");

        assertInvocationResult().booleanO(true);
    }
}
