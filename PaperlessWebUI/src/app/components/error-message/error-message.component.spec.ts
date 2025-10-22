import { TestBed } from '@angular/core/testing';
import { ErrorMessageComponent } from './error-message.component';

describe('ErrorMessageComponent', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({});
    });

    it('Error message should display text', () => {
        // Given
        const text = 'error message';

        // When
        const fixture = TestBed.createComponent(ErrorMessageComponent);

        fixture.componentRef.setInput('text', text);
        fixture.detectChanges();

        // Then
        const nativeElement = fixture.nativeElement as HTMLElement;
        expect(fixture.componentInstance).toBeDefined();
        expect(nativeElement.textContent).toContain(text);
    });
});
