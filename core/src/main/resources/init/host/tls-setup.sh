#!/bin/bash

wait_for_salt_bootstrap() {
  while ! curl -s -m 5 127.0.0.1:7070/saltboot/health; do echo -n .; sleep 1; done
}

generate_ca_cert() {
  curl 127.0.0.1:7070/saltboot/ca
  body=$(jq -n "{servers: [{name:\"\", address: \"127.0.0.1\"}], PublicIP: \"${publicIp}\"}" -c)
  pass=$(sudo cat /etc/salt-bootstrap/security-config.yml | grep password | cut -d" " -f2)
  curl -X POST -u "cbadmin:$pass" -d "$body" 127.0.0.1:7070/saltboot/clientcreds
}

start_nginx() {
  sudo mv /tmp/nginx.conf /etc/nginx/nginx.conf
  sudo mkdir -p /usr/share/nginx/json/
  sudo mv /tmp/50x.json /usr/share/nginx/json/50x.json
  sudo service nginx start
  sudo chkconfig nginx on
}

setup_tls() {
  wait_for_salt_bootstrap
  generate_ca_cert
  start_nginx
}

main() {
  set -x
  setup_tls 2>&1 | tee /home/${username}/tls-setup.log
}

[[ "$0" == "$BASH_SOURCE" ]] && main "$@"
