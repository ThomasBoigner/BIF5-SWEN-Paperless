export const environment = {
    paperlessRestUrl: 'http://localhost:8081',
    keycloak: {
        authority: 'http://localhost:8084',
        redirectUri: 'http://localhost:4200',
        urlPattern: /^(http:\/\/localhost:8081)(\/.*)?$/i,
    },
};
