base:
  '*':
    - consul
    - unbound
    - java
    - metadata

  'roles:kerberos_server':
    - match: grain
    - kerberos

  'G@roles:ambari_upgrade and G@roles:ambari_agent':
    - match: compound
    - ambari.agent-upgrade
    - smartsense.agent-upgrade

  'G@roles:ambari_upgrade and G@roles:ambari_server':
    - match: compound
    # smartsense needs to run before the Ambari server upgrade, because it needs a running server
    - smartsense.server-upgrade
    - ambari.server-upgrade

  'roles:gateway':
    - match: grain
    - gateway

  'roles:ambari_server':
    - match: grain
    - prometheus.server
    - ambari.server
    - grafana

  'roles:ambari_agent':
    - match: grain
    - ambari.agent

  'roles:smartsense':
    - match: grain
    - smartsense

  'recipes:pre':
    - match: grain
    - pre-recipes

  'recipes:post':
    - match: grain
    - post-recipes
