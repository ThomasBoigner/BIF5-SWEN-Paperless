import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';
import { ErrorMessageComponent } from '../../components/error-message/error-message.component';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FileButtonComponent } from '../../components/file-button/file-button.component';
import { FileMetaDataService } from '../../service/file-meta-data.service';
import { UploadFileCommand } from '../../model/commands/upload-file-command';
import { ErrorResponse } from '../../model/exception/error-response';
import { HttpErrorResponse } from '@angular/common/http';
import { TextAreaInputComponent } from '../../components/textarea-input/textarea-input.component';
import { FileInputComponent } from '../../components/file-input/file-input.component';

@Component({
    selector: 'upload-file-page',
    templateUrl: './upload-file-page.component.html',
    styleUrls: ['./upload-file-page.component.css'],
    imports: [
        RouterLink,
        NgOptimizedImage,
        ErrorMessageComponent,
        TextAreaInputComponent,
        FileButtonComponent,
        FormsModule,
        ReactiveFormsModule,
        FileInputComponent,
    ],
})
export class UploadFilePageComponent {
    errorMessage = '';

    fileForm = new FormGroup({
        file: new FormControl<File | null>(null),
        description: new FormControl<string | null>(null),
    });

    constructor(
        private fileMetaDataService: FileMetaDataService,
        private router: Router,
    ) {}

    onFileSelected(event: Event) {
        const input = event.target as HTMLInputElement;
        if (!input.files || input.files.length === 0) {
            return;
        }
        const file = input.files[0];
        this.fileForm.patchValue({ file });
    }

    handleSubmit() {
        const description = this.fileForm.controls.description.value;

        const command: UploadFileCommand = {
            description: description?.trim().length !== 0 ? description : null,
        };
        const file = this.fileForm.controls.file.value;

        if (file == null) {
            this.errorMessage = 'No file selected.';
            return;
        }

        this.fileMetaDataService.uploadFile(file, command).subscribe({
            complete: () => void this.router.navigate(['/']),
            error: (error: HttpErrorResponse) => {
                this.errorMessage = (error.error as ErrorResponse).message;
            },
        });
    }
}
