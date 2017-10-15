import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UniversityEditComponent } from './university-edit.component';

describe('UniversityEditComponent', () => {
  let component: UniversityEditComponent;
  let fixture: ComponentFixture<UniversityEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UniversityEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UniversityEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
