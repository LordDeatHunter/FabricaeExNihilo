// priority: 0

console.info('Hello, World! (You will only see this line once in console, during startup)')

onEvent('item.registry', event => {
	// Register new items here
	// event.create('example_item').displayName('Example Item')
})

onEvent('block.registry', event => {
    event.create('test_block', block => {
        block.displayName('Test Block');
        block.material('wood');
        block.hardness(1.0);
    })

    event.create('crushed_test_block', block => {
        block.displayName('Crushed Test Block');
        block.material('aggregate');
        block.hardness(0.6);
    });
})