import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';
import { ErrorMessageComponent } from '../../components/error-message/error-message.component';
import { TextInputComponent } from '../../components/text-input/text-input.component';
import { FormControl, FormGroup, FormsModule } from '@angular/forms';
import { FileButtonComponent } from '../../components/file-button/file-button.component';

@Component({
    selector: 'upload-file-page',
    templateUrl: './upload-file-page.component.html',
    styleUrls: ['./upload-file-page.component.css'],
    imports: [
        RouterLink,
        NgOptimizedImage,
        ErrorMessageComponent,
        TextInputComponent,
        FileButtonComponent,
        FormsModule,
    ],
})
export class UploadFilePageComponent {
    errorMessage = '';

    fileForm = new FormGroup({
        file: new FormControl<File | null>(null),
        description: new FormControl<string | null>(null),
    });
}
