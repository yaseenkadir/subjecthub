import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditUniversityComponent } from './edit-university.component';

describe('EditUniversityComponent', () => {
  let component: EditUniversityComponent;
  let fixture: ComponentFixture<EditUniversityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditUniversityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditUniversityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
