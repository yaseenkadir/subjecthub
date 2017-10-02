import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubjectCommentsComponent } from './subject-comments.component';

describe('SubjectCommentsComponent', () => {
  let component: SubjectCommentsComponent;
  let fixture: ComponentFixture<SubjectCommentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SubjectCommentsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubjectCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
