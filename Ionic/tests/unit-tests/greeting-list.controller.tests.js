describe('GreetingListPage', function() {

  describe('#getGreetings', function() {
    it('should call getGreetings on greetingService', function() {
      expect(greetingServiceMock.getGreetings).toHaveBeenCalled();
    });

    describe('when the get is executed', function() {
      it('if successful, should populate greetings variable', function {
        // TODO
      });

      it('if unsuccessful, should log the error in the console', function {
        // TODO
      })
    });
  });
});
