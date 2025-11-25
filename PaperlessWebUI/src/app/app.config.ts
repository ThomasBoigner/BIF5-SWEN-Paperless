import {
    ApplicationConfig,
    importProvidersFrom,
    provideBrowserGlobalErrorListeners,
    provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';
import { AutoRefreshTokenService, provideKeycloak, UserActivityService } from 'keycloak-angular';
import { environment } from '../environments/environment';

export const appConfig: ApplicationConfig = {
    providers: [
        provideBrowserGlobalErrorListeners(),
        provideZoneChangeDetection({ eventCoalescing: true }),
        provideRouter(routes),
        provideHttpClient(withFetch()),
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
