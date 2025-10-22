import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FileButtonComponent } from './file-button.component';

describe('FileButtonComponent', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({});
    });

    it('Text should be displayed and color should be set', () => {
        // Given
        const text = 'hello world';

        // When
        const fixture: ComponentFixture<FileButtonComponent> =
            TestBed.createComponent(FileButtonComponent);

        fixture.componentRef.setInput('text', text);
        fixture.componentRef.setInput('color', 'primary');
        fixture.detectChanges();

        // Then
        const nativeElement = fixture.nativeElement as HTMLElement;
        expect(fixture.componentInstance).toBeDefined();
        expect(nativeElement.textContent).toContain(text);
        expect(nativeElement.querySelector('button')?.classList).toContain('primary');
    });
});
