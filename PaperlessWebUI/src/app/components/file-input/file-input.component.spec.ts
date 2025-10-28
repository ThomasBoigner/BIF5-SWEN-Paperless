import {ComponentFixture, TestBed} from "@angular/core/testing";
import {FormControl, FormGroup} from "@angular/forms";
import {FileInputComponent} from "./file-input.component";

describe('FileInputComponent', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({});
    });

    it('Should bind to passed in form group', () => {
        // Given
        const file = new File(["foo"], "foo.txt")
        const dataTransfer = new DataTransfer();
        dataTransfer.items.add(file);
        const label = 'Message';
        const formGroup = new FormGroup({
            file: new FormControl<string>(""),
        });

        // When
        const fixture: ComponentFixture<FileInputComponent> =
            TestBed.createComponent(FileInputComponent);

        fixture.componentRef.setInput('label', label);
        fixture.componentRef.setInput('formGroup', formGroup);
        fixture.componentRef.setInput('controlName', 'file');
        fixture.detectChanges();

        const nativeElement = fixture.nativeElement as HTMLElement;
        const input = nativeElement.querySelector('input') ?? new HTMLInputElement();

        input.files = dataTransfer.files
        input.dispatchEvent(new Event('input'));
        //input.dispatchEvent(new Event('change'));
        fixture.detectChanges();

        // Then
        expect(fixture.componentInstance).toBeDefined();
        expect(nativeElement.textContent).toContain(label);
        const formObject = fixture.componentInstance.formGroup().value as { file: string };
        expect(formObject.file).toEqual("C:\\fakepath\\foo.txt");
    });
})