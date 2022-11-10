import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YesnodialogComponent } from './yesnodialog.component';

describe('YesnodialogComponent', () => {
  let component: YesnodialogComponent;
  let fixture: ComponentFixture<YesnodialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ YesnodialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(YesnodialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
