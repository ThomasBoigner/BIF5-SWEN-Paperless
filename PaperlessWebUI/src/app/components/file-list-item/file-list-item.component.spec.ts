import {TestBed} from "@angular/core/testing";
import {provideRouter} from "@angular/router";
import {routes} from "../../app.routes";
import {FileListItemComponent} from "./file-list-item.component";

describe("FileListItemComponent", () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [provideRouter(routes)],
        });
    })

    it("File data should be displayed", () => {
        // Given
        const fileMetaData = {
            fileName: 'File-1.pdf',
            description: 'This is the description of the first file.',
            fileToken: 'abc',
            fileSize: 100,
            creationDate: new Date(),
            fullText: '',
            summary: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ...',
        }

        // When
        const fixture = TestBed.createComponent(FileListItemComponent);

        fixture.componentRef.setInput("fileMetaData", fileMetaData)
        fixture.detectChanges();

        // Then
        const nativeElement = fixture.nativeElement as HTMLElement;
        expect(fixture.componentInstance).toBeDefined()
        expect(nativeElement.textContent).toContain(fileMetaData.fileName);
        expect(nativeElement.textContent).toContain(fileMetaData.description);
    })
})