import { TestBed } from '@angular/core/testing';
import { LoggerTestingModule } from 'ngx-logger/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { FileMetaDataService } from './file-meta-data.service';
import { FileMetaData } from '../model/file-meta-data';

describe('FileMetaDataService', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [LoggerTestingModule],
            providers: [provideHttpClient(), provideHttpClientTesting()],
        });
    });

    afterEach(() => {
        TestBed.inject(HttpTestingController).verify();
    });

    it('#getAllFileMetaData should return all file meta data from the server', () => {
        // Given
        const fileMetaDataService = TestBed.inject(FileMetaDataService);
        const expectedFileMetaData: FileMetaData[] = [
            {
                fileName: 'File-1.pdf',
                description: 'This is the description of the first file.',
                fileToken: 'abc',
                fileSize: 100,
                creationDate: new Date(),
                fullText: '',
                summary: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ...',
            },
            {
                fileName: 'File-2.pdf',
                description: 'This is the description of the second file.',
                fileToken: 'abc',
                fileSize: 200,
                creationDate: new Date(),
                fullText: '',
                summary: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ...',
            },
        ];

        // When
        const response = fileMetaDataService.getAllFileMetaData();

        // Then
        response.subscribe((fileMetaData) => {
            expect(fileMetaData).toEqual(expectedFileMetaData);
        });

        const req = TestBed.inject(HttpTestingController).expectOne({
            method: 'GET',
            url: 'http://localhost:8081/api/files',
        });

        req.flush(expectedFileMetaData);
        expect(req.request.responseType).toEqual('json');
    });
});
