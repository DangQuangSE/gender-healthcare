# Zoom demo placeholder repair

The old showcase data may contain generated `example.com/demo-consultation/...`
links. The SQL artifact clears only that exact generated prefix and preserves
real provider links in either column.

Run it through the approved database change process:

1. Back up the database and save the dry-run rows.
2. Verify the physical table and column names against the deployed schema.
3. Execute the update in staging first.
4. Review `repaired_rows` and the post-update rows.
5. Commit the transaction only after the result contains no generated demo link.

The application currently has no `DemoDataSeeder.java` source in this branch,
so there is no active runtime seeder to edit. New application-created online
appointments already start without meeting links; this artifact handles the
historical data that may remain in the database.
