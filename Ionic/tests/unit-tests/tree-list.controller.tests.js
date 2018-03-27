describe('TreeListPage', function() {

  describe('#getTrees', function() {
    it('should call getTrees on treeService', function() {
      expect(treeServiceMock.getTrees).toHaveBeenCalled();
    });

    describe('when the get is executed', function() {
      it('if successful, should populate trees variable', function {
        // TODO
      });

      it('if unsuccessful, should log the error in the console', function {
        // TODO
      })
    });
  });
});
