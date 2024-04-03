import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RawViewComponent } from './raw-view.component';

describe('RawViewComponent', () => {
  let component: RawViewComponent;
  let fixture: ComponentFixture<RawViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RawViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RawViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
