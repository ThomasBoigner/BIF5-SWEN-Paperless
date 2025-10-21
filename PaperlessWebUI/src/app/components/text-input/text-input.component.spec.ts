import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TextInputComponent } from './text-input.component';
import { FormControl, FormGroup } from '@angular/forms';

describe('TextInputComponent', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({});
    });

    it('Should bind to passed in form group', () => {
        // Given
        const text = 'hello world';
        const label = 'Message';
        const formGroup = new FormGroup({
            text: new FormControl<string>(''),
        });

        // When
        const fixture: ComponentFixture<TextInputComponent> =
            TestBed.createComponent(TextInputComponent);

        fixture.componentRef.setInput('label', label);
        fixture.componentRef.setInput('formGroup', formGroup);
        fixture.componentRef.setInput('controlName', 'text');
        fixture.detectChanges();

        const nativeElement = fixture.nativeElement as HTMLElement;
        const input = nativeElement.querySelector('input') ?? new HTMLInputElement();

        input.value = text;
        input.dispatchEvent(new Event('input'));
        fixture.detectChanges();

        // Then
        expect(fixture.componentInstance).toBeDefined();
        expect(nativeElement.textContent).toContain(label);
        const formObject = fixture.componentInstance.formGroup().value as { text: string };
        expect(formObject.text).toEqual(text);
    });
});
