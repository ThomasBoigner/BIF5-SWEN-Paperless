import { Routes } from '@angular/router';
import { MainPageComponent } from './pages/main-page/main-page.component';
import { UploadFilePageComponent } from './pages/upload-file-page/upload-file-page.component';

export const routes: Routes = [
    {
        path: '',
        title: 'Paperless',
        component: MainPageComponent,
    },
    {
        path: 'file/view/:token',
        title: 'Paperless',
        component: MainPageComponent,
    },
    {
        path: 'file/upload',
        title: 'Upload a file!',
        component: UploadFilePageComponent,
    },
];
