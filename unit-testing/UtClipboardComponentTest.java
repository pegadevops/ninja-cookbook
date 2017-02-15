package component;

public class UtClipboardComponentTest extends ComponentTestSupport {
    @Test
    public void activity_primary_plus_pageList() throws Exception {
        logTestStart();
        PreparePageList preparePxResults = preparePage("TestList123").create(C_CODE_PEGA_LIST).pageList(P_PX_RESULTS);
        preparePxResults.append(C_BASECLASS).prop(P_PY_ID, "first");
        preparePxResults.append(C_BASECLASS).prop(P_PY_ID, "second");
        preparePxResults.append(C_BASECLASS).prop(P_PY_ID, "third");
        preparePage("EmptyList").create(C_CODE_PEGA_LIST);

        invoke().activity().className(C_CODE_PEGA_LIST).name("TestPageList").primaryPage("TestList123");

        AssertPageList assertPxResults = assertPage("TestList123").pageList(P_PX_RESULTS);
        assertPxResults.at(1).prop(P_PY_LABEL, "first #1");
        assertPxResults.at(2).prop(P_PY_LABEL, "second #2");
        assertPxResults.at(3).prop(P_PY_LABEL, "third #3");
        assertPxResults.size(3);
        AssertPageList assertEmptyPxResults = assertPage("EmptyList").pageList(P_PX_RESULTS);
        assertEmptyPxResults.notExists();
        assertEmptyPxResults.size(0);
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void activity_pageGroup() throws Exception {
        logTestStart();
        PreparePageGroup testGroup = preparePage("TestPage123").create(C_WORK_UT).pageGroup("TestPageGroup");
        testGroup.add("FIRST", C_DATA).prop(P_PY_LABEL, "first label");
        testGroup.add("SECOND", C_DATA).prop(P_PY_LABEL, "label 2");
        testGroup.add("THIRD", C_DATA).prop(P_PY_LABEL, "label #three");

        invoke().activity().className(C_WORK_UT).name("TestPageGroup").primaryPage("TestPage123");

        AssertPageGroup assertTestGroup = assertPage("TestPage123").pageGroup("TestPageGroup");
        AssertPageGroup assertEmptyTestGroup = assertPage("TestPage123").pageGroup("TestPageGroup2");
        assertTestGroup.at("FIRST").prop(P_PY_DESCRIPTION, "first label : FIRST");
        assertTestGroup.at("SECOND").prop(P_PY_DESCRIPTION, "label 2 : SECOND");
        assertTestGroup.at("THIRD").prop(P_PY_DESCRIPTION, "label #three : THIRD");
        assertTestGroup.size(3);
        assertEmptyTestGroup.notExists();
        assertEmptyTestGroup.size(0);
        assertActivityStatus().good().messageAny();
    }

    @Test
    public void prepare_create_page() throws Exception {
        logTestStart();
        preparePage("PrimPage").create(C_WORK_UT)
                .prop("SearchKey", "test search key")
                .page("TestNestedPage").create(C_BASECLASS)
                .prop(P_PY_ID, "nested id");

        preparePage(G_PY_WORK_PAGE).create(C_WORK_UT).pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id1");
        preparePage(G_PY_WORK_PAGE).create(C_WORK_UT).pageList("IdList").append(C_BASECLASS).prop(P_PY_ID, "id2");

        assertPage("PrimPage").exists().page("TestNestedPage").exists().prop(P_PY_ID, "nested id");
        assertPage(G_PY_WORK_PAGE).exists().pageList("IdList").size(1).at(1).prop(P_PY_ID, "id2");
    }
}
