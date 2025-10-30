import { Component } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { NgOptimizedImage } from '@angular/common';
import { ErrorMessageComponent } from '../../components/error-message/error-message.component';
import { TextAreaInputComponent } from '../../components/textarea-input/textarea-input.component';
import { FileButtonComponent } from '../../components/file-button/file-button.component';
import { FormControl, FormGroup } from '@angular/forms';
import { FileMetaDataService } from '../../service/file-meta-data.service';
import { UpdateFileCommand } from '../../model/commands/update-file-command';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorResponse } from '../../model/exception/error-response';

@Component({
    selector: 'update-file-page',
    templateUrl: './update-file-page.component.html',
    imports: [
        RouterLink,
        NgOptimizedImage,
        ErrorMessageComponent,
        TextAreaInputComponent,
        FileButtonComponent,
    ],
    styleUrls: ['./update-file-page.component.css'],
})
export class UpdateFilePageComponent {
    errorMessage = '';

    fileToken?: string;
    fileForm = new FormGroup({
        description: new FormControl<string | null>(null),
    });

    constructor(
        private fileMetaDataService: FileMetaDataService,
        private route: ActivatedRoute,
        private router: Router,
    ) {
        this.route.paramMap.subscribe((paramMap) => {
            const fileToken = paramMap.get('token');

            if (!fileToken) {
                return;
            }

            this.fileToken = fileToken;
            fileMetaDataService.getFileMetaData(fileToken).subscribe((fileMetaData) => {
                this.fileForm = new FormGroup({
                    description: new FormControl<string | null>(fileMetaData.description ?? null),
                });
            });
        });
    }

    handleSubmit() {
        if (!this.fileToken) {
            this.errorMessage = 'Tour can not be found!';
            return;
        }
        const command: UpdateFileCommand = {
            description: this.fileForm.controls.description.value,
        };
        this.fileMetaDataService.updateFile(this.fileToken, command).subscribe({
            complete: () =>
                void this.router.navigate([this.fileToken ? `/file/view/${this.fileToken}` : '/']),
            error: (error: HttpErrorResponse) => {
                this.errorMessage = (error.error as ErrorResponse).message;
            },
        });
    }
}
