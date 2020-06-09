import java.util.regex.Matcher
import java.util.regex.Pattern

enum Bump {
    MINOR,
    MAJOR,
    PATCH,
    RELEASE,
}

class Semver {

    int major, minor, patch
    String prerel, build

    private final static String NAT = '0|[1-9][0-9]*'
    private final static String ALPHANUM = '[0-9]*[A-Za-z-][0-9A-Za-z-]*'
    private final static String IDENT = "$NAT|$ALPHANUM"
    private final static String FIELD = '[0-9A-Za-z-]+'
    private final static Pattern SEMVER_REGEX = ~"^[vV]?($NAT)\\.($NAT)\\.($NAT)(\\-(${IDENT})(\\.(${IDENT}))*)?(\\+${FIELD}(\\.${FIELD})*)?\$"

    Semver(String version) {
// Matcher groups
//        0. ALL
//        1. MAJOR
//        2. MINOR
//        3. PATCH
//        4. PRERELEASE
//        8. BUILD_METADATA
        Matcher matcher = SEMVER_REGEX.matcher(version)
        if (matcher.matches()) {
            this.major = matcher.group(1).toInteger()
            this.minor = matcher.group(2).toInteger()
            this.patch = matcher.group(3).toInteger()
            this.prerel = matcher.group(4)?.substring(1)
            this.build = matcher.group(8)?.substring(1)
        } else {
            throw new IllegalArgumentException("Invalid semantic version ´${version}´")
        }
    }

    private Semver(int major, int minor, int patch) {
        this.major = major
        this.minor = minor
        this.patch = patch
    }

    static Semver parse(String version) throws IllegalArgumentException {
        return new Semver(version)
    }

    static boolean validate(String version) {
        try {
            parse(version)
            true
        } catch (IllegalArgumentException ignored) {
            false
        }
    }

    Semver patch() {
        return bump(Bump.PATCH)
    }

    Semver minor() {
        return bump(Bump.MINOR)
    }

    Semver major() {
        return bump(Bump.MAJOR)
    }

    Semver prerel(String prerel) {
        return new Semver(release() + opt(prerel, '-'))
    }

    Semver build(String build) {
        return new Semver(release() + opt(prerel, '-') + opt(build, '+'))
    }

    Semver bump(Bump bump) {
        Semver result
        switch (bump) {
            case Bump.PATCH:
                if (prerel || build) {
                    result = new Semver(major, minor, patch)
                } else {
                    result = new Semver(major, minor, patch + 1)
                }
                break
            case Bump.MINOR:
                result = new Semver(major, minor + 1, 0)
                break
            case Bump.MAJOR:
                result = new Semver(major + 1, 0, 0)
                break
            case Bump.RELEASE:
                result = new Semver(major, minor, patch)
                break
            default:
                throw new IllegalArgumentException("Bump ´${bump}´ is unkown")
        }
        return result
    }

    String text() {
        return "${release()}" + opt(prerel, '-') + opt(build, '+')
    }

    private static String opt(String value, String sep) {
        return (value ? "${sep}${value}" : "")
    }

    String release() {
        return "${major}.${minor}.${patch}"
    }

}
