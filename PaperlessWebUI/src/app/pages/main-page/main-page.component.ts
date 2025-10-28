import { Component } from '@angular/core';
import { AsyncPipe, DatePipe, NgOptimizedImage } from '@angular/common';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { FileButtonComponent } from '../../components/file-button/file-button.component';
import { SearchBarComponent } from '../../components/search-bar/search-bar.component';
import { FileMetaData } from '../../model/file-meta-data';
import { Observable } from 'rxjs';
import { FileMetaDataService } from '../../service/file-meta-data.service';
import { FileListItemComponent } from '../../components/file-list-item/file-list-item.component';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { FileSizePipe } from '../../pipes/file-size-pipe';
import { SmallFileButton } from '../../components/small-file-button/small-file-button.component';

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
        FileSizePipe,
        SmallFileButton,
    ],
    styleUrls: ['./main-page.component.css'],
})
export class MainPageComponent {
    fileMetaData$: Observable<FileMetaData> | undefined;
    fileMetaDataList$: Observable<FileMetaData[]>;

    mainContentMode: 'pdf' | 'text' | 'summary' = 'pdf';

    leftSidebar = true;
    rightSidebar = true;

    constructor(
        private fileMetaDataService: FileMetaDataService,
        private route: ActivatedRoute,
        private router: Router
    ) {
        this.route.paramMap.subscribe((paramMap) => {
            const fileToken = paramMap.get('token');
            if (fileToken) {
                this.fileMetaData$ = fileMetaDataService.getFileMetaData(fileToken);
            }
        });
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

    deleteFile(token: string) {
        this.fileMetaDataService.deleteFile(token).subscribe(() => void this.router.navigate(['/']))
    }
}
