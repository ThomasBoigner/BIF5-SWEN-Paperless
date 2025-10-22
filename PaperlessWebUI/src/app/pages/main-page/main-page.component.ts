import { Component } from '@angular/core';
import { DatePipe, NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FileButtonComponent } from '../../components/file-button/file-button.component';
import { SearchBarComponent } from '../../components/search-bar/search-bar.component';
import { FileMetaData } from '../../model/file-meta-data';

@Component({
    selector: 'main-page',
    templateUrl: './main-page.component.html',
    imports: [NgOptimizedImage, RouterLink, FileButtonComponent, SearchBarComponent, DatePipe],
    styleUrls: ['./main-page.component.css'],
})
export class MainPageComponent {
    fileMetaData: FileMetaData = {
        fileName: 'File-1.pdf',
        description: 'This is the description of the file.',
        fileToken: 'abc',
        fileSize: 100,
        creationDate: new Date(),
        fullText: '',
        summary: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ...',
    };
}
