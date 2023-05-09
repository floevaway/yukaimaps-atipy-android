# example of env variables to use the app with a local seed

# The seed has no HTTPS. Allow insecure requests
export OIDC_INSECURE=true

# API will be accessed by wifi in your local network.
# you need to know your private IP in this network
# ubuntu : hostname -I
export API_ROOT=http://192.168.1.11:3000
export OIDC_SERVER=http://192.168.1.11:3030/realms/yukaimaps

# this is the default client configured in the seed.
# /!\ The clien need to authorize a special redirect uri :
# `org.yukaimaps://oidc`
export OAUTH_CLIENT_ID=local-id

