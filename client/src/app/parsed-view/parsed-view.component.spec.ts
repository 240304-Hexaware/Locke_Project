import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParsedViewComponent } from './parsed-view.component';

describe('ParsedViewComponent', () => {
  let component: ParsedViewComponent;
  let fixture: ComponentFixture<ParsedViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParsedViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ParsedViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
