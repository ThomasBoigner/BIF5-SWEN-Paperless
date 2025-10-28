import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SmallFileButton } from './small-file-button.component';

describe('SmallFileButtonComponent', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({});
    });

    it('Text should be displayed', () => {
        // Given
        const text = 'hello world';

        // When
        const fixture: ComponentFixture<SmallFileButton> = TestBed.createComponent(SmallFileButton);

        fixture.componentRef.setInput('text', text);
        fixture.detectChanges();

        // Then
        const nativeElement = fixture.nativeElement as HTMLElement;
        expect(fixture.componentInstance).toBeDefined();
        expect(nativeElement.textContent).toContain(text);
    });
});
