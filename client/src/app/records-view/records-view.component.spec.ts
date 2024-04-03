import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecordsViewComponent } from './records-view.component';

describe('RecordsViewComponent', () => {
  let component: RecordsViewComponent;
  let fixture: ComponentFixture<RecordsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecordsViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RecordsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
