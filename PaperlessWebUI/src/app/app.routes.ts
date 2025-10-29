import { Routes } from '@angular/router';
import { MainPageComponent } from './pages/main-page/main-page.component';
import { UploadFilePageComponent } from './pages/upload-file-page/upload-file-page.component';
import {UpdateFilePageComponent} from "./pages/update-file-page/update-file-page.component";

export const routes: Routes = [
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
    {
        path: 'file/update/:token',
        title: 'Update a file!',
        component: UpdateFilePageComponent,
    },
    {
        path: '**',
        title: 'Paperless',
        component: MainPageComponent,
    },
];
