import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SearchBarComponent } from './search-bar.component';

describe('SearchBarComponent', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({});
    });

    it('Text value should be two way bindable', () => {
        // Given
        const text = 'Hello world';
        const placeholder = 'placeholder';

        // When
        const fixture: ComponentFixture<SearchBarComponent> =
            TestBed.createComponent(SearchBarComponent);

        fixture.componentRef.setInput('placeholder', placeholder);

        const nativeElement = fixture.nativeElement as HTMLElement;
        const input = nativeElement.querySelector('input') ?? new HTMLInputElement();

        fixture.detectChanges();
        input.value = text;
        input.dispatchEvent(new Event('input'));
        fixture.detectChanges();

        // Then
        expect(fixture.componentInstance).toBeDefined();
        expect(fixture.componentInstance.value()).toEqual(text);
    });
});
