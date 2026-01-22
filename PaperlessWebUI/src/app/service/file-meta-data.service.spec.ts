import { TestBed } from '@angular/core/testing';
import { LoggerTestingModule } from 'ngx-logger/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { FileMetaDataService } from './file-meta-data.service';
import { FileMetaData } from '../model/file-meta-data';
import { UploadFileCommand } from '../model/commands/upload-file-command';

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
                numberOfAccesses: 5,
                fullText: '',
                summary: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ...',
            },
            {
                fileName: 'File-2.pdf',
                description: 'This is the description of the second file.',
                fileToken: 'abc',
                fileSize: 200,
                creationDate: new Date(),
                numberOfAccesses: 5,
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
            url: 'http://localhost:80/api/files?query=',
        });

        req.flush(expectedFileMetaData);
        expect(req.request.responseType).toEqual('json');
    });

    it('#getFileMetaData should return file meta data from the server', () => {
        // Given
        const fileMetaDataService = TestBed.inject(FileMetaDataService);
        const expectedFileMetaData = {
            fileName: 'File-1.pdf',
            description: 'This is the description of the first file.',
            fileToken: 'abc',
            fileSize: 100,
            numberOfAccesses: 5,
            creationDate: new Date(),
            fullText: '',
            summary: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ...',
        };

        // When
        const response = fileMetaDataService.getFileMetaData('abc');

        // Then
        response.subscribe((fileMetaData) => {
            expect(fileMetaData).toEqual(expectedFileMetaData);
        });

        const req = TestBed.inject(HttpTestingController).expectOne({
            method: 'GET',
            url: 'http://localhost:80/api/files/abc',
        });

        req.flush(expectedFileMetaData);
        expect(req.request.responseType).toEqual('json');
    });

    it('#upload file should upload a file to the server and return its metadata', () => {
        const fileMetaDataService = TestBed.inject(FileMetaDataService);
        const expectedFileMetaData = {
            fileName: 'File-1.pdf',
            description: 'This is the description of the first file.',
            fileToken: 'abc',
            fileSize: 100,
            creationDate: new Date(),
            numberOfAccesses: 5,
            fullText: '',
            summary: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ...',
        };

        const file = new File(['foo'], 'foo.txt');

        const uploadFileCommand: UploadFileCommand = {
            description: 'Upload a file to the server',
        };

        // When
        const response = fileMetaDataService.uploadFile(file, uploadFileCommand);

        // Then
        response.subscribe((fileMetaData) => {
            expect(fileMetaData).toEqual(expectedFileMetaData);
        });

        const req = TestBed.inject(HttpTestingController).expectOne({
            method: 'POST',
            url: 'http://localhost:80/api/files',
        });

        req.flush(expectedFileMetaData);
        expect(req.request.responseType).toEqual('json');
    });

    it('#update file should update the file meta data on the server and then return its metadata', () => {
        const fileMetaDataService = TestBed.inject(FileMetaDataService);
        const expectedFileMetaData = {
            fileName: 'File-1.pdf',
            description: 'This is the description of the first file.',
            fileToken: 'abc',
            fileSize: 100,
            creationDate: new Date(),
            numberOfAccesses: 5,
            fullText: '',
            summary: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ...',
        };
        const updateFileMetaDataCommand: UploadFileCommand = {
            description: 'abc',
        };

        // When
        const response = fileMetaDataService.updateFile(
            expectedFileMetaData.fileToken,
            updateFileMetaDataCommand,
        );

        // Then
        response.subscribe((fileMetaData) => {
            expect(fileMetaData).toEqual(expectedFileMetaData);
        });

        const req = TestBed.inject(HttpTestingController).expectOne({
            method: 'PUT',
            url: `http://localhost:80/api/files/${expectedFileMetaData.fileToken}`,
        });

        req.flush(expectedFileMetaData);
        expect(req.request.responseType).toEqual('json');
    });

    it('#deleteFile should delete a file on the server', () => {
        // Given
        const fileMetaDataService = TestBed.inject(FileMetaDataService);

        // When
        fileMetaDataService.deleteFile('abc').subscribe();

        // Then
        const req = TestBed.inject(HttpTestingController).expectOne({
            method: 'DELETE',
            url: 'http://localhost:80/api/files/abc',
        });

        req.flush(null, { status: 200, statusText: 'ok' });
    });
});
