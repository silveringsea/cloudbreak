#!/bin/bash

## logging
exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1

set -x

export CLOUD_PLATFORM="${cloudPlatform}"
export START_LABEL=${platformDiskStartLabel}
export PLATFORM_DISK_PREFIX=${platformDiskPrefix}
export LAZY_FORMAT_DISK_LIMIT=12
export IS_GATEWAY=${gateway?c}
export TMP_SSH_KEY="${tmpSshKey}"
export PUBLIC_SSH_KEY="${publicSshKey}"
export RELOCATE_DOCKER=${relocateDocker?c}
export SSH_USER=${sshUser}
export SALT_BOOT_PASSWORD=${saltBootPassword}
export SALT_BOOT_SIGN_KEY=${signaturePublicKey}

${customUserData}

/usr/bin/user-data-helper.sh "$@" &> /var/log/user-data.log

service salt-bootstrap stop
curl -Lo salt-bootstrap https://www.dropbox.com/s/sk3oower73pvgdx/salt-bootstrap?dl=0
chmod +x salt-bootstrap
mv -f salt-bootstrap /usr/sbin/salt-bootstrap
service salt-bootstrap start