require recipes-images/images/phytec-headless-image.bb

SUMMARY = "This image is designed to show development of a Slint UI \
           application running on Wayland."
LICENSE = "MIT"

IMAGE_FEATURES += " \
    splash \
    ssh-server-openssh \
    hwcodecs \
    weston \
"

IMAGE_INSTALL += " \
    packagegroup-base \
    packagegroup-gstreamer \
    weston \
    weston-init \
    weston-xwayland \
    coffee-app \
"
