import { Component } from '@angular/core';
import { AsyncPipe, DatePipe, NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FileButtonComponent } from '../../components/file-button/file-button.component';
import { SearchBarComponent } from '../../components/search-bar/search-bar.component';
import { FileMetaData } from '../../model/file-meta-data';
import { Observable } from 'rxjs';
import { FileMetaDataService } from '../../service/file-meta-data.service';
import { FileListItemComponent } from '../../components/file-list-item/file-list-item.component';
import { PdfViewerModule } from 'ng2-pdf-viewer';

@Component({
    selector: 'main-page',
    templateUrl: './main-page.component.html',
    imports: [
        NgOptimizedImage,
        RouterLink,
        FileButtonComponent,
        SearchBarComponent,
        DatePipe,
        AsyncPipe,
        FileListItemComponent,
        PdfViewerModule,
    ],
    styleUrls: ['./main-page.component.css'],
})
export class MainPageComponent {
    leftSidebar = true;
    rightSidebar = true;

    fileMetaDataList$: Observable<FileMetaData[]>;
    fileMetaData: FileMetaData = {
        fileName: 'File-1.pdf',
        description: 'This is the description of the file.',
        fileToken: 'abc',
        fileSize: 100,
        creationDate: new Date(),
        fullText: '',
        summary: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ...',
    };

    constructor(private fileMetaDataService: FileMetaDataService) {
        this.fileMetaDataList$ = this.fileMetaDataService.getAllFileMetaData();
    }

    setLeftSidebar(leftSidebar: boolean) {
        this.leftSidebar = leftSidebar;
        window.dispatchEvent(new Event('resize'));
    }

    setRightSidebar(rightSidebar: boolean) {
        this.rightSidebar = rightSidebar;
        window.dispatchEvent(new Event('resize'));
    }
}
