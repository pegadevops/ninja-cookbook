package component;

/**
 * Component tests covering all ninja unit test functionality (run on Pega). As component tests, they require deployment of Ninja Webapp.
 * You can use component tests as the specification of the ninja-UT features (ninja UT cookbook).
 */
@Category(ComponentTests.class)
@RunWith(NinjaRemoteRunner.class)
public abstract class ComponentTestSupport extends NinjaTestSupport implements TestConstants {
}
