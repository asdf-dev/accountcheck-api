package accountcheck

@SuppressWarnings("JavaIoPackageAccess") //we want a file in this case
class SpecController {

    def showSpec() {
        render file: spec, fileName: "spec-csgo.yaml", contentType: "application/octet-stream"
    }

    protected static File getSpec() {
        new File("build/resources/main/openapi/spec.yaml")
    }
}
