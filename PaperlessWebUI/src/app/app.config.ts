import {
    ApplicationConfig,
    importProvidersFrom,
    provideBrowserGlobalErrorListeners,
    provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import {
    AutoRefreshTokenService,
    INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG, includeBearerTokenInterceptor,
    provideKeycloak,
    UserActivityService
} from 'keycloak-angular';
import { environment } from '../environments/environment';

export const appConfig: ApplicationConfig = {
    providers: [
        provideBrowserGlobalErrorListeners(),
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideRouter(routes),
        provideHttpClient(withInterceptors([includeBearerTokenInterceptor])),
        {
            provide: INCLUDE_BEARER_TOKEN_INTERCEPTOR_CONFIG,
            useValue: [{
                urlPattern: /^(http:\/\/localhost:8081)(\/.*)?$/i,
                bearerPrefix: 'Bearer'
            }]
        },
        provideKeycloak({
            config: {
                url: environment.keycloak.authority,
                realm: 'paperless',
                clientId: 'paperless-web-ui',
            },
            initOptions: {
                onLoad: 'login-required',
            },
            providers: [AutoRefreshTokenService, UserActivityService],
        }),
        importProvidersFrom(
            LoggerModule.forRoot({
                level: NgxLoggerLevel.TRACE,
                serverLogLevel: NgxLoggerLevel.TRACE,
            }),
        ),
    ],
};
