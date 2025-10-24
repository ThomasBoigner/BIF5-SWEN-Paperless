import { Component, input } from '@angular/core';
import { NgOptimizedImage } from '@angular/common';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ImageData } from '../image-data';

@Component({
    selector: 'file-input',
    templateUrl: './file-input.component.html',
    imports: [NgOptimizedImage, ReactiveFormsModule],
    styleUrls: ['./file-input.component.css'],
})
export class FileInputComponent {
    label = input.required<string>();
    formGroup = input.required<FormGroup>();
    controlName = input.required<string>();
    image = input<ImageData>();
}
