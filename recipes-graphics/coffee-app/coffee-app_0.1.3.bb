SUMMARY = "Slint UI Coffee App Demo"
LICENSE = "MIT"
HOMEPAGE = "https://www.phytec.de/"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    https://download.phytec.de/Software/Linux/Applications/next-gen-coffee-app-v0_1_3.zip \
    file://coffee-app.service \
    file://coffee-app-mock.service \
"
SRC_URI[sha256sum] = "f7b196fbc2b36b49ffb8dca91d3cfd81b6187985e6d559988751b47f775b5590"

S = "${WORKDIR}/next-gen-coffee-app"

inherit systemd

PACKAGES += "${PN}-mock"

SYSTEMD_SERVICE:${PN} = "coffee-app.service"
SYSTEMD_SERVICE:${PN}-mock = "coffee-app-mock.service"

do_install:append() {
    install -d ${D}${bindir}
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${libexecdir}/${BPN}

    install -m 0755 ${S}/target/aarch64-unknown-linux-gnu/release/NextCoffee ${D}${bindir}/coffee-app
    install -m 0644 ${WORKDIR}/coffee-app.service ${D}${systemd_system_unitdir}/coffee-app.service

    install -m 0644 ${S}/dist/auto_coreservice_mock.js ${D}${libexecdir}/${BPN}
    install -m 0644 ${WORKDIR}/coffee-app-mock.service ${D}${systemd_system_unitdir}/coffee-app-mock.service
}

FILES:${PN} = " \
    ${bindir}/coffee-app \
    ${systemd_system_unitdir}/coffee-app.service \
"
FILES:${PN}-mock = " \
    ${libexecdir}/${BPN} \
    ${systemd_system_unitdir}/coffee-app-mock.service \
"
RDEPENDS:${PN} = "libxrandr fontconfig freetype ${PN}-mock"
RDEPENDS:${PN}-mock += "nodejs nodejs-npm"
